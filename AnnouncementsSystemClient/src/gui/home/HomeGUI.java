package gui.home;

import gui.account.DeleteAccountGUI;
import gui.account.ReadAccountGUI;
import gui.account.UpdateAccountGUI;
import gui.category.CreateCategoryGUI;
import gui.category.DeleteCategoryGUI;
import gui.category.ReadCategoryGUI;
import gui.category.UpdateCategoryGUI;
import gui.connection.ConnectionGUI;
import gui.ClientWindow;
import main.Client;
import operations.authentication.LogoutOperation;
import responses.Response;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;

public class HomeGUI {
    private final Client client;
    private final ClientWindow clientWindow;

    public HomeGUI(Client client, ClientWindow clientWindow) {
        this.client = client;
        this.clientWindow = clientWindow;

        setupGUI();
    }

    private void setupGUI() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));

        String windowTitle = (client.isAdmin()) ? "Painel do Admin" : "Painel do Usuário";
        JLabel lblWindowTitle = new JLabel(windowTitle);
        lblWindowTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        contentPane.add(lblWindowTitle, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(center, BorderLayout.CENTER);

        JPanel accountPanel = new JPanel();
        accountPanel.setBorder(new TitledBorder(null, "Conta", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        center.add(accountPanel);

        JButton btnReadAccount = new JButton("Ver dados");
        btnReadAccount.addActionListener(_ -> new ReadAccountGUI(client, clientWindow));
        accountPanel.add(btnReadAccount);

        JButton btnUpdateAccount = new JButton("Atualizar dados");
        btnUpdateAccount.addActionListener(_ -> new UpdateAccountGUI(client, clientWindow));
        accountPanel.add(btnUpdateAccount);

        JButton btnDeleteAccount = new JButton("Excluir");
        btnDeleteAccount.addActionListener(_ -> new DeleteAccountGUI(client, clientWindow));
        accountPanel.add(btnDeleteAccount);

        if(client.isAdmin()) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.setBorder(new TitledBorder(null, "Categoria de Avisos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            center.add(categoryPanel);

            JButton btnCreateCategory = new JButton("Criar");
            btnCreateCategory.addActionListener(_ -> new CreateCategoryGUI(client, clientWindow));
            categoryPanel.add(btnCreateCategory);

            JButton btnReadCategory = new JButton("Ver");
            btnReadCategory.addActionListener(_ -> new ReadCategoryGUI(client, clientWindow));
            categoryPanel.add(btnReadCategory);

            JButton btnUpdateCategory = new JButton("Atualizar");
            btnUpdateCategory.addActionListener(_ -> new UpdateCategoryGUI(client, clientWindow));
            categoryPanel.add(btnUpdateCategory);

            JButton btnDeleteCategory = new JButton("Excluir");
            btnDeleteCategory.addActionListener(_ -> new DeleteCategoryGUI(client, clientWindow));
            categoryPanel.add(btnDeleteCategory);
        }

        JButton btnLogout = new JButton("Fazer logout e desconectar");
        btnLogout.addActionListener(_ -> logoutActionHandler());
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        clientWindow.showContentPane(contentPane);
    }

    private void logoutActionHandler() {
        LogoutOperation logoutOp = new LogoutOperation(client.getLoggedInUserToken());
        String responseJson;

        try {
            responseJson = client.sendToServer(logoutOp);
        } catch (IOException e) {
            clientWindow.showErrorMessage("Erro ao comunicar com o servidor", e.getLocalizedMessage());
            return;
        }

        Response logoutResponse = new Response(responseJson);
        String responseCode = logoutResponse.getResponseCode();
        String message = logoutResponse.getMessage();

        if(!responseCode.equals("010")) {
            clientWindow.showErrorMessage("Erro ao realizar logout", message);
            return;
        }

        try {
            client.disconnect();
        } catch (IOException e) {
            clientWindow.showErrorMessage("Erro ao desconectar", e.getLocalizedMessage());
            clientWindow.dispose();
            return;
        }

        new ConnectionGUI(clientWindow);
        clientWindow.showSuccessMessage(message);
    }
}
