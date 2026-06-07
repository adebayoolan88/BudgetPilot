import { Button } from "@/components/ui/button"
import { Link } from "react-router-dom"

function Navbar() {
  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-slate-900 border-b border-slate-800">
      <div className="max-w-7xl mx-auto px-6 flex items-center justify-between h-16">
        
        {/* Logo */}
        <div className="flex items-center gap-2">
          <span className="text-xl font-bold text-white">BudgetPilot</span>
        </div>

        {/* Nav Links */}
        <nav className="hidden md:flex items-center gap-8">
          <a href="#features" className="text-sm text-slate-300 hover:text-white transition-colors">
            Features
          </a>
        </nav>

        {/* Buttons */}
        <div className="hidden md:flex items-center gap-3">
          <Button variant="ghost" className="text-slate-300 hover:text-white">
            Sign In
          </Button>
          <Button className="bg-emerald-600 hover:bg-emerald-700 text-white">
            Get Started
          </Button>
        </div>

      </div>
    </header>
  )
}

export default Navbar