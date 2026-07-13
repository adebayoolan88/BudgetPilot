# BudgetPilot 💰

AI-powered personal finance app. Track income, budget expenses, and get intelligent financial insights powered by Claude.

## Stack

- **Frontend** — React 19, Vite, shadcn/ui, Tailwind CSS
- **Auth** — Clerk (Google/email sign-in, synced to a backend user record)
- **Backend** — Spring Boot 4 (Java 21), Spring Security (JWT resource server, per-user authorization)
- **Database** — PostgreSQL
- **AI** — Anthropic Claude API (Haiku 4.5), with multi-turn conversation memory grounded in the user's real financial data

## Features

- Track multiple income streams
- Budget expenses by category (rent, car notes, subscriptions, food, etc.), with full create/edit/delete
- Log transactions, with a dedicated correction flow for fixing data-entry mistakes (transactions aren't meant to be casually edited once money has moved)
- Set savings goals and track contributions with visual progress
- Pick a budgeting strategy (e.g. 50/30/20) and see actual spend vs. target by category
- Personal dashboard with monthly income/expense/net overview and savings goal progress
- AI financial assistant — ask Claude questions about your budget, get personalized recommendations grounded in your actual income/expenses/goals, and continue a real back-and-forth conversation
- Backend enforces per-user authorization on every request — a valid session can only ever read or write its own data

## Getting Started

### Backend
```bash
cd backend/budgetpilot
cp .env.example .env   # fill in your DB credentials, Clerk JWKS URL, and an Anthropic API key
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend/budgetpilot
cp .env.example .env   # fill in your Clerk publishable key
npm install
npm run dev
```

## Roadmap

- Deploy to AWS EC2 with CloudWatch monitoring
- CI/CD via GitHub Actions

## Status

🚧 Active Development — core budgeting features and the AI assistant are functional end-to-end locally; not yet deployed.

## Author

**Adebayo Olaniyan** — UTSA Computer Science
