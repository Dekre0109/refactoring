package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    /**
     * Create a new statement printer for the given invoice and plays.
     *
     * @param invoice the invoice to print
     * @param plays   the mapping from play ID to play details
     */
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Return a plain text statement for this printer's invoice.
     *
     * @return the formatted statement string
     */
    public String statement() {
        final StringBuilder result = new StringBuilder(
                String.format("Statement for %s%n", invoice.getCustomer()));

        // build a line for each performance
        for (Performance performance : invoice.getPerformances()) {
            Play play = getPlay(performance);
            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    play.getName(),
                    usd(getAmount(performance)),
                    performance.getAudience()));
        }

        // totals
        result.append(String.format("Amount owed is %s%n", usd(getTotalAmount())));
        result.append(String.format("You earned %s credits%n", getTotalVolumeCredits()));
        return result.toString();
    }

    /**
     * Look up the play for the given performance.
     *
     * @param performance the performance
     * @return the corresponding play
     */
    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Compute the base amount (in cents) for a single performance.
     *
     * @param performance the performance
     * @return the amount in cents
     */
    private int getAmount(Performance performance) {
        int result;
        Play play = getPlay(performance);

        switch (play.getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format(
                        "unknown type: %s", play.getType()));
        }

        return result;
    }

    /**
     * Compute the volume credits for a single performance.
     *
     * @param performance the performance
     * @return the volume credits earned
     */
    private int getVolumeCredits(Performance performance) {
        int result = Math.max(
                performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        // extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }

    /**
     * Format an amount (in cents) as a US dollar currency string.
     *
     * @param amount the amount in cents
     * @return the formatted currency string
     */
    private String usd(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(amount / Constants.PERCENT_FACTOR);
    }

    /**
     * Compute the total amount (in cents) for all performances.
     *
     * @return the total amount in cents
     */
    private int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    /**
     * Compute the total volume credits for all performances.
     *
     * @return the total volume credits
     */
    private int getTotalVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }
}
