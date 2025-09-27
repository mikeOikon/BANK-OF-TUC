ΙΔΕΕΣ ΑΠΟ ΟΜΑΔΙΚΗ:
ΠΕΜΠΤΗ 25/9 -->
ΑΓΓΕΛΟΣ: USERS: 1) EMPLOYERS 2) ADMINS 3) AUDITORS
         CUSTOMERS: 1) PERSONAL ACCOUNT               2) BUISINESS CUSTOMER
                       a) SAVINGS ACCOUNT                 a) BUISINESS ACCOUNT
                       b) TRANSACTIONS ACCOUNT
                       c) FIXED TERM ACCOUNT
ΠΑΡΑΣΚΕΥΗ 26/9 --> 
ΜΙΧΑΗΛ: 2. Λειτουργικές Απαιτήσεις (Functional Requirements)
🔑 2.1 Διαχείριση Χρηστών

FR1: Ο χρήστης μπορεί να εγγραφεί στο σύστημα ως:

Πελάτης (φυσικό πρόσωπο)

Επιχείρηση (νομικό πρόσωπο)

Διαχειριστής (admin)

FR2: Ο χρήστης συνδέεται με username & password.

FR3: Ο διαχειριστής μπορεί να εγκρίνει/απενεργοποιεί λογαριασμούς.

💳 2.2 Διαχείριση Λογαριασμών

FR4: Ο χρήστης μπορεί να δει το υπόλοιπο και τις κινήσεις κάθε λογαριασμού.

FR5: Δυνατότητα δημιουργίας νέου λογαριασμού (για πελάτες/επιχειρήσεις).

FR6: Καταγραφή κινήσεων (statements) με:

Ημερομηνία

Ποσό

Αιτιολογία

Υπόλοιπο μετά τη συναλλαγή

💸 2.3 Συναλλαγές

FR7: Κατάθεση σε λογαριασμό.

FR8: Ανάληψη (με έλεγχο διαθεσίμων).

FR9: Μεταφορά χρημάτων:

Εσωτερικά (ίδια τράπεζα)

Διατραπεζικά (SEPA/SWIFT simulation)

FR10: Πληρωμή λογαριασμών επιχειρήσεων.

📅 2.4 Αυτοματισμοί

FR11: Πάγιες εντολές (π.χ. κάθε 1η του μήνα).

FR12: Αυτόματος τοκισμός λογαριασμών στο τέλος κάθε περιόδου.

FR13: Προσομοίωση χρόνου (day-by-day simulation) για έλεγχο αυτοματισμών.

🏦 2.5 Διαχείριση Επιχειρήσεων

FR14: Οι επιχειρήσεις μπορούν να ανεβάζουν λογαριασμούς πληρωμής (προς πελάτες).

FR15: Οι επιχειρήσεις μπορούν να βλέπουν ποιοι πελάτες έχουν πληρώσει.

🛠 2.6 Διοικητικές Λειτουργίες

FR16: Διαχείριση χρηστών (προσθήκη/αφαίρεση).

FR17: Διαχείριση επιτοκίων/προμηθειών.

FR18: Παραγωγή αναφορών (στατιστικά συναλλαγών).

📌 3. Μη Λειτουργικές Απαιτήσεις (Non-Functional Requirements)
🔐 Ασφάλεια

NFR1: Όλες οι συνδέσεις απαιτούν ταυτοποίηση.

NFR2: Οι συναλλαγές θα απαιτούν επιβεβαίωση πριν την ολοκλήρωση.

NFR3: Τα δεδομένα πρέπει να αποθηκεύονται με ασφαλή τρόπο (π.χ. hashed κωδικοί).

⚡ Απόδοση

NFR4: Οι βασικές λειτουργίες (π.χ. εμφάνιση υπολοίπου) να εκτελούνται σε <1 sec.

NFR5: Το σύστημα πρέπει να υποστηρίζει τουλάχιστον 50 ταυτόχρονους χρήστες.

💻 Ευχρηστία

NFR6: Διπλή διεπαφή (GUI + CLI).

NFR7: Ευανάγνωστες αναφορές συναλλαγών.

🔄 Συντηρησιμότητα / Επεκτασιμότητα

NFR8: Να είναι εύκολη η προσθήκη νέων τύπων μεταφορών (π.χ. SEPA Instant).

NFR9: Κώδικας modular, με χρήση των patterns (Singleton, Factory, Command, DAO).

📌 4. Σενάρια Χρήσης (Use Case Examples)

UC1: Ο πελάτης συνδέεται, βλέπει υπόλοιπο, κάνει μεταφορά σε φίλο, αποσυνδέεται.


UC2: Η επιχείρηση ανεβάζει τιμολόγιο -> πελάτης το βλέπει -> πληρώνει online.

UC3: Ο διαχειριστής αλλάζει το επιτόκιο -> επηρεάζονται αυτόματα οι μελλοντικοί τοκισμοί.
ΜΙΧΑΗΛ: Ο ADMIN ΝΑ ΕΙΝΑΙ ΕΝΑΣ EMPLOYER ΠΟΥ ΑΝ ΘΕΛΕΙ ΝΑ ΚΑΝΕΙ ΕΠΙΠΛΕΟΝ ΠΡΑΓΜΑΤΑ ΝΑ ΧΡΕΙΑΖΕΤΑΙ ACCESS KEY
ΑΓΓΕΛΟΣ: ΑΦΟΥ ΕΧΟΥΜΕΚΑΙ ΤΟΝ ΕΛΕΓΚΤΗ ΑΣ ΒΑΛΟΥΜΕ ΕΝΑ ΑΡΧΙΚΟ FRONTEND ΚΑΙ ΑΝΑΛΟΓΑ ΜΕ ΤΟ USERNAME KAI KEY CODE ΝΑ ΜΠΑΙΝΕΙ ΚΑΤΕΥΘΕΙΑΝ ΣΕ ΛΕΙΤΟΥΡΓΙΑ CUSTOMER/EMPLOYER/ADMIN
MΙΧΑΗΛ: Ο USER ΠΟΥ ΦΤΙΑΧΝΕΙ ΤΟ ΔΙΚΟ ΤΟΥ USERNAME NA ΤΟΥ ΔΙΝΟΥΜΕ ΕΝΑ GENERATED USERID ΚΑΙ ΜΕΤΑ ΝΑ ΥΠΑΡΧΧΕΙ ΕΠΙΛΟΓΗ LOGIN WITH USERID OR USERNAME
ΑΓΓΕΛΟΣ: ΣΥΜΦΕΡΕΙ ΠΡΟΓΡΑΜΜΑΤΙΣΤΙΚΑ ΝΑ ΦΤΙΑΞΟΥΜΕ ΕΝΑ FRONTEND ΚΑΙ ΝΑ ΜΠΑΙΝΕΙ ΜΕ ΤΟ USERNAME ΣΤΟ ΑΝΤΙΣΤΟΙΧΟ ΡΟΛΟ ΑΥΤΟΜΑΤΑ
(ΕΠΕΙΤΑ ΑΠΟ ΣΥΖΗΤΗΣΗ ΕΚΤΕΝΗ): ΜΙΧΑΗΛ: ΝΑ ΚΑΝΟΥΜΕ 2 INTERFACES ΕΝΑ ΓΙΑ CUSTOMERS ΕΝΑ ΓΙΑ EMPLOYERS/ADMINS
ΑΓΓΕΛΟΣ: ΝΑΙ ΑΥΤΟ ΤΟ ΑΚΟΥΩ, ΑΥΤΟ ΝΑ ΚΑΝΟΥΜΕ
ΣΑΒΒΑΤΟ 27/9 --> ΑΓΓΕΛΟΣ: ΑΤΕΛΝΩ ΕΝΑ ΚΟΜΜΑΤΙ ΚΩΔΙΚΑ ΠΟΥ ΒΡΗΚΑ ΓΙΑ ΤΟ ΙΒΑΝ: το ελληνικό IBAN έχει 27 χαρακτήρες: GR + 2 check digits + BBAN (23 chars) όπου BBAN = 3n (bank) + 4n (branch) + 16c (account number — συνήθως numeric padded). Αυτό επιβεβαιώνεται από το IBAN registry / τεκμηρίωση.
package backend.util;

import java.math.BigInteger;

public class GreekIbanUtil {

    private static final String COUNTRY_CODE = "GR";
    private static final int BBAN_LENGTH = 23; // 3 + 4 + 16
    private static final int ACCOUNT_NUMBER_LENGTH = 16;

    /**
     * Δημιουργεί έγκυρο ελληνικό IBAN από bank (3 digits), branch (4 digits) και accountNumber (μέχρι 16 chars).
     *
     * @param bank3  τριψήφιος κωδικός τράπεζας (π.χ. "011")
     * @param branch4 τετραψήφιος κωδικός υποκαταστήματος (π.χ. "0125")
     * @param account ατομικός αριθμός λογαριασμού (θα γεμίσει αριστερά με '0' για να φτάσει 16 chars)
     * @return IBAN string (χωρίς κενά), π.χ. "GR1601101250000000012300695"
     * @throws IllegalArgumentException αν οι παράμετροι δεν έχουν σωστή μορφή
     */
    public static String generateGreekIban(String bank3, String branch4, String account) {
        if (!bank3.matches("\\d{3}")) {
            throw new IllegalArgumentException("bank3 πρέπει να είναι 3 ψηφία");
        }
        if (!branch4.matches("\\d{4}")) {
            throw new IllegalArgumentException("branch4 πρέπει να είναι 4 ψηφία");
        }
        if (account == null || account.length() > ACCOUNT_NUMBER_LENGTH || account.length() == 0) {
            throw new IllegalArgumentException("account πρέπει να έχει μήκος 1.." + ACCOUNT_NUMBER_LENGTH);
        }

        // pad account αριστερά με 0 μέχρι 16 chars
        String accPadded = String.format("%1$" + ACCOUNT_NUMBER_LENGTH + "s", account).replace(' ', '0');

        // BBAN = bank3 + branch4 + accPadded
        String bban = bank3 + branch4 + accPadded;

        if (bban.length() != BBAN_LENGTH) {
            throw new IllegalStateException("BBAN μήκος λάθος: " + bban.length());
        }

        // Για υπολογισμό check digits, φτιάχνουμε string: BBAN + COUNTRY_CODE + "00"
        String rearranged = bban + COUNTRY_CODE + "00";

        // Μετατροπή χαρακτήρων => αριθμητικά (A=10..Z=35)
        String numericRepresentation = toNumericString(rearranged);

        // υπολογισμός mod 97
        BigInteger num = new BigInteger(numericRepresentation);
        int mod97 = num.mod(BigInteger.valueOf(97)).intValue();

        int checkDigitsInt = 98 - mod97;
        String checkDigits = (checkDigitsInt < 10) ? ("0" + checkDigitsInt) : Integer.toString(checkDigitsInt);

        return COUNTRY_CODE + checkDigits + bban;
    }

    /**
     * Ελέγχει αν ένα IBAN είναι έγκυρο (μορφή + MOD97).
     *
     * @param iban IBAN με ή χωρίς κενά
     * @return true αν έγκυρο
     */
    public static boolean validateIban(String iban) {
        if (iban == null) return false;
        String compact = iban.replaceAll("\\s+", "").toUpperCase();

        // Γενικός έλεγχος χώρας και μήκους (Ελλάδα = 27)
        if (compact.length() != 27) return false;
        if (!compact.startsWith(COUNTRY_CODE)) return false;

        // Μετακινούμε τους πρώτους 4 χαρακτήρες στο τέλος (country + check)
        String rearranged = compact.substring(4) + compact.substring(0, 4);

        String numericRepresentation = toNumericString(rearranged);
        BigInteger num = new BigInteger(numericRepresentation);
        return num.mod(BigInteger.valueOf(97)).intValue() == 1;
    }

    // Μετατρέπει string που μπορεί να περιέχει γράμματα και ψηφία σε αριθμητική αναπαράσταση
    // όπου Α->10, B->11, ..., Z->35. Τα ψηφία παραμένουν ως έχουν.
    private static String toNumericString(String input) {
        StringBuilder sb = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                sb.append(ch);
            } else if (Character.isLetter(ch)) {
                int val = Character.toUpperCase(ch) - 'A' + 10;
                sb.append(val);
            } else {
                throw new IllegalArgumentException("Μη επιτρεπτός χαρακτήρας στο IBAN/BBAN: " + ch);
            }
        }
        return sb.toString();
    }


  ΑΓΓΕΛΟΣ:ΕΙΝΑΙ ΑΠΟ ΤΣΑΤ ΑΠΛΑ ΓΙΑ ΝΑ ΕΧΟΥΜΕ ΜΙΑ ΕΙΚΟΝΑ, ΚΑΘΕ ΤΡΑΠΕΖΑ ΕΧΕΙ ΕΝΑΝ 3ΨΗΦΙΟ ΑΡΙΘΜΟ
  ΜΙΧΑΗΛ:302
  ΑΓΓΕΛΟΣ:666
  (ΔΕΝ ΕΧΟΥΜΕ ΚΑΤΑΛΗΞΕΙ)
