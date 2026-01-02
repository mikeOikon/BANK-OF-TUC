package frontend.gui.tabs;

import backend.BankSystem;
import backend.users.User;
import services.user_services.UpdateEmailCommand;
import services.user_services.UpdatePhoneNumberCommand;

import javax.swing.*;
import java.awt.*;

public class ProfileTab extends JPanel implements Refreshable{

	private JPanel emailContainer;
	private JTextField emailField;
	private JLabel emailLabel;

	private JPanel phoneContainer;
	private JTextField phoneField;
	private JLabel phoneLabel;

	private JButton saveBtn;

    private final User user;

    public ProfileTab(User user) {

    	this.user = user;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("User Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        form.add(new JLabel("Username:"));
        form.add(new JLabel(user.getUsername()));

        form.add(new JLabel("Name:"));
        form.add(new JLabel(user.getName() + " " + user.getSurname()));


        form.add(new JLabel("Email:"));
        emailContainer = new JPanel(new BorderLayout());
        form.add(emailContainer);

        form.add(new JLabel("Phone Number:"));
        phoneContainer = new JPanel(new BorderLayout());
        form.add(phoneContainer);

        add(form, BorderLayout.CENTER);

        this.saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> saveAndRefresh());

        JPanel footer = new JPanel();
        footer.add(this.saveBtn);
        add(footer, BorderLayout.SOUTH);

        refresh();
    }
    @Override
    public void refresh() {

        // -------- EMAIL --------
        emailContainer.removeAll();

        boolean needsEmail = (user.getEmail() == null || user.getEmail().isBlank());

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            emailField = new JTextField();
            emailContainer.add(emailField, BorderLayout.CENTER);
        } else {
            emailLabel = new JLabel(user.getEmail());
            emailContainer.add(emailLabel, BorderLayout.CENTER);
        }

        // -------- PHONE --------
        phoneContainer.removeAll();
        boolean needsPhone = (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank());

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            phoneField = new JTextField();
            phoneContainer.add(phoneField, BorderLayout.CENTER);
        } else {
            phoneLabel = new JLabel(user.getPhoneNumber());
            phoneContainer.add(phoneLabel, BorderLayout.CENTER);
        }

        if (!needsEmail && !needsPhone) {
            saveBtn.setVisible(false);
        } else {
            saveBtn.setVisible(true);
        }

        emailContainer.revalidate();
        emailContainer.repaint();
        phoneContainer.revalidate();
        phoneContainer.repaint();
    }


    private void saveAndRefresh() {
        boolean changedSomething = false;

        // ---------- EMAIL ----------
        if (emailField != null) {
            String email = emailField.getText() == null ? "" : emailField.getText().trim();

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Το email δεν μπορεί να είναι κενό.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // basic email validation (simple, not perfect)
            if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Μη έγκυρο email.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                UpdateEmailCommand command = new UpdateEmailCommand(user, email);
                command.execute();
                changedSomething = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα ενημέρωσης email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // ---------- PHONE ----------
        if (phoneField != null) {
            String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();

            // Αν δεν συμπλήρωσε τίποτα, ΜΗΝ τρέξεις command → αυτό λύνει το error που βλέπεις
            if (!phone.isEmpty()) {

                // basic phone validation: only digits, length 10 (προσαρμόζεις αν θες)
                if (!phone.matches("^\\d{10}$")) {
                    JOptionPane.showMessageDialog(this, "Το τηλέφωνο πρέπει να έχει 10 ψηφία.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    UpdatePhoneNumberCommand command = new UpdatePhoneNumberCommand(user, phone);
                    command.execute();
                    changedSomething = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Σφάλμα ενημέρωσης τηλεφώνου: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (changedSomething) {
            BankSystem.getInstance().saveAllData();
            refresh();
            JOptionPane.showMessageDialog(this, "Profile updated");
        } else {
            JOptionPane.showMessageDialog(this, "Δεν έγινε καμία αλλαγή.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }




}
