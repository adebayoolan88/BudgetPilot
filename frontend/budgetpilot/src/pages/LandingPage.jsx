import Navbar from "../components/Navbar"

const features = [
  {
    title: "Expense Tracking",
    description: "Automatically categorize and track every dollar you spend with intelligent transaction monitoring."
  },
  {
    title: "Budget Planning",
    description: "Create custom budgets for different categories and get real-time alerts when you're approaching limits."
  },
  {
    title: "Savings Goals",
    description: "Set and track multiple savings goals with visual progress indicators and smart recommendations."
  },
  {
    title: "AI Financial Assistant",
    description: "Get personalized insights and recommendations powered by advanced AI to optimize your finances."
  }
]

function LandingPage() {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col">
      <Navbar />

      {/* Hero */}
      <div className="pt-40 flex flex-col items-center text-center gap-4 max-w-2xl mx-auto">
        <p className="text-white text-5xl font-bold">
          Smarter Budgeting. Powered by AI.
        </p>
        <hr className="border-slate-600 w-full" />
        <p className="text-slate-300 text-lg">
          Track expenses, set savings goals, and get AI-powered insights — all for free.
        </p>
      </div>

      {/* Features */}
      <div id ="features" className="mt-56 max-w-6xl mx-auto px-6 flex flex-col items-center gap-12">
        <div className="text-center">
          <p className="text-white text-3xl font-bold">Everything You Need to Master Your Money</p>
          <p className="text-slate-400 mt-2">Powerful features designed to help you track, plan, and grow your finances.</p>
        </div>
        <div className="grid grid-cols-4 gap-6 w-full ">
          {features.map((feature) => (
            <div key={feature.title} className="bg-slate-900 border border-slate-800 rounded-xl p-6 flex flex-col gap-3 min-h-88">
              <p className="text-white font-semibold text-lg">{feature.title}</p>
              <p className="text-slate-400 text-sm">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Footer */}
      <footer className="mt-auto pt-16 pb-8 text-center text-slate-500 text-sm flex flex-col gap-2">
        <p>Built by Adebayo Olaniyan</p>
        <div className="flex justify-center gap-4">
          <a href="https://github.com/adebayoolan88" target="_blank" className="hover:text-white transition-colors">GitHub</a>
          <a href="https://linkedin.com/in/adebayo-olaniyan-jr" target="_blank" className="hover:text-white transition-colors">LinkedIn</a>
        </div>
      </footer>

    </div>
  )
}

export default LandingPage