import { useEffect, useRef, useState } from "react"
import ReactMarkdown from "react-markdown"
import { useApi } from "@/services/api"
import { aiConversationService } from "@/services/aiConversationService"
import { useCurrentUser } from "@/context/CurrentUserContext"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Textarea } from "@/components/ui/textarea"
import { Send } from "lucide-react"

const MARKDOWN_CLASSES =
  "[&_p]:mb-2 [&_p:last-child]:mb-0 [&_strong]:font-semibold " +
  "[&_h1]:text-base [&_h1]:font-semibold [&_h1]:mt-3 [&_h1]:mb-1 [&_h1:first-child]:mt-0 " +
  "[&_h2]:text-base [&_h2]:font-semibold [&_h2]:mt-3 [&_h2]:mb-1 [&_h2:first-child]:mt-0 " +
  "[&_h3]:text-sm [&_h3]:font-semibold [&_h3]:mt-2 [&_h3]:mb-1 " +
  "[&_ul]:list-disc [&_ul]:pl-5 [&_ul]:mb-2 [&_ol]:list-decimal [&_ol]:pl-5 [&_ol]:mb-2 " +
  "[&_li]:mb-0.5 [&_a]:underline [&_code]:bg-background/50 [&_code]:rounded [&_code]:px-1"

function AiAssistant() {
  const api = useApi()
  const currentUser = useCurrentUser()
  const [conversations, setConversations] = useState([])
  const [loading, setLoading] = useState(true)
  const [message, setMessage] = useState("")
  const [sending, setSending] = useState(false)
  const scrollRef = useRef(null)

  async function loadConversations() {
    const data = await aiConversationService.getAllForUser(api, currentUser.id)
    setConversations([...data].reverse())
    setLoading(false)
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- fetch-on-mount
    loadConversations()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth" })
  }, [conversations])

  async function handleSend(event) {
    event.preventDefault()
    if (!message.trim()) return
    setSending(true)
    try {
      const created = await aiConversationService.create(api, currentUser.id, message)
      setConversations((prev) => [...prev, created])
      setMessage("")
    } finally {
      setSending(false)
    }
  }

  return (
    <div className="flex flex-col gap-6 h-[calc(100vh-10rem)]">
      <div>
        <h1 className="text-2xl font-semibold">AI Assistant</h1>
        <p className="text-muted-foreground text-sm">
          Ask questions about your budget and get personalized recommendations.
        </p>
      </div>

      <Card className="flex-1 flex flex-col overflow-hidden">
        <CardContent className="flex-1 overflow-y-auto flex flex-col gap-4">
          {loading ? (
            <p className="text-muted-foreground text-sm">Loading conversation...</p>
          ) : conversations.length === 0 ? (
            <p className="text-muted-foreground text-sm">
              Ask about your spending, savings goals, or how to hit a budgeting target.
            </p>
          ) : (
            conversations.map((conversation) => (
              <div key={conversation.id} className="flex flex-col gap-2">
                <div className="self-end bg-primary text-primary-foreground rounded-lg px-3 py-2 max-w-[80%] text-sm">
                  {conversation.message}
                </div>
                <div
                  className={`self-start bg-muted text-foreground rounded-lg px-3 py-2 max-w-[80%] text-sm ${MARKDOWN_CLASSES}`}
                >
                  <ReactMarkdown>{conversation.response}</ReactMarkdown>
                </div>
              </div>
            ))
          )}
          <div ref={scrollRef} />
        </CardContent>
      </Card>

      <form onSubmit={handleSend} className="flex gap-2 items-end">
        <Textarea
          placeholder="Ask BudgetPilot AI..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault()
              handleSend(e)
            }
          }}
          className="min-h-11"
        />
        <Button type="submit" disabled={sending || !message.trim()}>
          <Send />
          {sending ? "Sending..." : "Send"}
        </Button>
      </form>
    </div>
  )
}

export default AiAssistant
