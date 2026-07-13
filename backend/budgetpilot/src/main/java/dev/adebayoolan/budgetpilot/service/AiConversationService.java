package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.model.Expense;
import dev.adebayoolan.budgetpilot.model.IncomeStream;
import dev.adebayoolan.budgetpilot.model.SavingGoal;
import dev.adebayoolan.budgetpilot.repository.AiConversationRepository;
import dev.adebayoolan.budgetpilot.repository.ExpenseRepository;
import dev.adebayoolan.budgetpilot.repository.IncomeStreamRepository;
import dev.adebayoolan.budgetpilot.repository.SavingGoalRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.AiConversation;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiConversationService {
    private final AiConversationRepository aiConversationRepository;
    private final UserRepository userRepository;
    private final IncomeStreamRepository incomeStreamRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingGoalRepository savingGoalRepository;
    private final ClaudeService claudeService;

    private static final String SYSTEM_PROMPT_INTRO = """
            You are the AI financial assistant inside BudgetPilot, a personal budgeting app.
            Answer the user's question and give practical, specific budgeting advice using the
            financial snapshot below. Keep responses concise and friendly.
            """;

    private static final int HISTORY_LIMIT = 10;

    public AiConversation createConversation(UUID userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<AiConversation> history = aiConversationRepository.findByUserOrderByCreatedAtDesc(user);
        Collections.reverse(history);
        if (history.size() > HISTORY_LIMIT) {
            history = history.subList(history.size() - HISTORY_LIMIT, history.size());
        }

        String systemPrompt = buildSystemPrompt(user);
        String reply = claudeService.sendMessage(systemPrompt, history, message);

        AiConversation conversation = new AiConversation();
        conversation.setUser(user);
        conversation.setMessage(message);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setResponse(reply);
        return aiConversationRepository.save(conversation);
    }

    private String buildSystemPrompt(User user) {
        List<IncomeStream> incomeStreams = incomeStreamRepository.findByUser(user);
        List<Expense> expenses = expenseRepository.findByUser(user);
        List<SavingGoal> savingGoals = savingGoalRepository.findByUser(user);

        StringBuilder snapshot = new StringBuilder(SYSTEM_PROMPT_INTRO);

        snapshot.append("\nIncome streams:\n");
        if (incomeStreams.isEmpty()) {
            snapshot.append("- none recorded\n");
        } else {
            for (IncomeStream income : incomeStreams) {
                snapshot.append("- %s: $%s (%s)\n".formatted(income.getName(), income.getAmount(),
                        income.getFrequency()));
            }
        }

        snapshot.append("\nExpenses:\n");
        if (expenses.isEmpty()) {
            snapshot.append("- none recorded\n");
        } else {
            for (Expense expense : expenses) {
                snapshot.append("- %s: $%s (%s)\n".formatted(expense.getName(), expense.getAmount(),
                        expense.getFrequency()));
            }
        }

        snapshot.append("\nSaving goals:\n");
        if (savingGoals.isEmpty()) {
            snapshot.append("- none recorded\n");
        } else {
            for (SavingGoal goal : savingGoals) {
                snapshot.append("- %s: $%s saved of $%s target\n".formatted(goal.getName(), goal.getCurrentAmount(),
                        goal.getTargetAmount()));
            }
        }

        return snapshot.toString();
    }

    public void deleteConversation(UUID id) {
        aiConversationRepository.deleteById(id);
    }

    public List<AiConversation> getConversationByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return aiConversationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public AiConversation getConversationById(UUID id) {
        return aiConversationRepository.findById(id).orElseThrow(() -> new RuntimeException("Conversation not found"));
    }
}
