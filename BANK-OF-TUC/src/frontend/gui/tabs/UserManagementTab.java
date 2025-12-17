
package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;



public class UserManagementTab extends JPanel {

    public UserManagementTab() {
        setLayout(new BorderLayout());
        add(new JLabel("User Management (Promote / Demote / Remove)",
                SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}
