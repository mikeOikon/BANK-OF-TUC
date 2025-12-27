package frontend.gui.tabs;

import backend.BankSystem;
import backend.users.User;
import services.UpdateEmailCommand;
import services.UpdatePhoneNumberCommand;

import javax.swing.*;
import java.awt.*;

public class ProfileTab extends JPanel {
	
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

        form.add(new JLabel("AFM:"));
        form.add(new JLabel(user.getAFM()));

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
    
    private void refresh() {

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

        if (emailField != null) {
        	UpdateEmailCommand command = new UpdateEmailCommand(user, emailField.getText());
        	command.execute();
        }

        if (phoneField != null) {
        	UpdatePhoneNumberCommand command = new UpdatePhoneNumberCommand(user, phoneField.getText());
        	command.execute();
        }
        
        BankSystem bank = BankSystem.getInstance();

        bank.dao.save(bank);
        
        refresh();

        JOptionPane.showMessageDialog(this, "Profile updated");
    }



}
