# BudgetPilot 💰

AI-powered personal finance app. Track income, budget expenses, and get intelligent financial insights powered by Claude.

## Stack

- **Frontend** — React, Vite, shadcn/ui, Tailwind CSS
- **Auth** — Clerk
- **Backend** — Spring Boot 4 (Java 21)
- **Database** — PostgreSQL
- **AI** — Anthropic Claude API
- **Infra** — AWS EC2, CloudWatch, GitHub Actions

## Features

- Track multiple income streams
- Budget expenses by category (rent, car notes, subscriptions, food, etc.)
- Personal dashboard with spending overview and savings goals
- AI financial assistant — ask Claude questions about your budget, run hypothetical scenarios, and get personalized recommendations

## Getting Started

### Backend
```bash
cd backend/budgetpilot
cp .env.example .env
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend/budgetpilot
npm install
npm run dev
```

## Status

🚧 Active Development

## Author

**Adebayo Olaniyan** — UTSA Computer Science
