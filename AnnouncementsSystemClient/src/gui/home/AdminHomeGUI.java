package gui.home;

import gui.account.DeleteAccountGUI;
import gui.account.ReadAccountGUI;
import gui.account.UpdateAccountGUI;
import gui.announcementcategory.CreateCategoryGUI;
import gui.announcementcategory.DeleteCategoryGUI;
import gui.announcementcategory.ReadCategoryGUI;
import gui.announcementcategory.UpdateCategoryGUI;
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

public class AdminHomeGUI {
    private final Client client;

    public AdminHomeGUI(Client client) {
        this.client = client;
    }

    public void setup() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));

        JLabel lblWindowTitle = new JLabel("Painel do Admin");
        lblWindowTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        contentPane.add(lblWindowTitle, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBorder(new EmptyBorder(5, 5, 5, 5));
        //center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        contentPane.add(center, BorderLayout.CENTER);

        JPanel accountPanel = new JPanel();
        accountPanel.setBorder(new TitledBorder(null, "Conta", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        center.add(accountPanel);

        JButton btnReadAccount = new JButton("Ver dados");
        btnReadAccount.addActionListener(_ ->
                new ReadAccountGUI(client).readAccount(client.getLoggedInUserId(), client.getLoggedInUserToken())
        );
        accountPanel.add(btnReadAccount);

        JButton btnUpdateAccount = new JButton("Atualizar dados");
        btnUpdateAccount.addActionListener(_ ->
                new UpdateAccountGUI(client).setup()
        );
        accountPanel.add(btnUpdateAccount);

        JButton btnDeleteAccount = new JButton("Excluir");
        btnDeleteAccount.addActionListener(_ ->
                new DeleteAccountGUI()
        );
        accountPanel.add(btnDeleteAccount);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setBorder(new TitledBorder(null, "Categoria de Avisos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        center.add(categoryPanel);

        JButton btnCreateCategory = new JButton("Criar");
        btnCreateCategory.addActionListener(_ ->
                new CreateCategoryGUI()
        );
        categoryPanel.add(btnCreateCategory);

        JButton btnReadCategory = new JButton("Ver");
        btnReadCategory.addActionListener(_ ->
                new ReadCategoryGUI()
        );
        categoryPanel.add(btnReadCategory);

        JButton btnUpdateCategory = new JButton("Atualizar");
        btnUpdateCategory.addActionListener(_ ->
                new UpdateCategoryGUI()
        );
        categoryPanel.add(btnUpdateCategory);

        JButton btnDeleteCategory = new JButton("Excluir");
        btnDeleteCategory.addActionListener(_ ->
                new DeleteCategoryGUI()
        );
        categoryPanel.add(btnDeleteCategory);

        JButton btnLogout = new JButton("Fazer logout e sair");
        btnLogout.addActionListener(_ -> logoutActionHandler());
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        client.showContentPane(contentPane);
    }

    private void logoutActionHandler() {
        LogoutOperation logoutOp = new LogoutOperation("6", client.getLoggedInUserToken());
        String json = logoutOp.toJson();
        String responseJson;

        try {
            responseJson = client.getServerConnection().sendToServer(json);
        } catch (IOException e) {
            client.showErrorMessage("Erro ao comunicar com o servidor", e.getLocalizedMessage());
            return;
        }

        Response logoutResponse = new Response(responseJson);
        String responseCode = logoutResponse.getResponseCode();
        String message = logoutResponse.getMessage();

        if(responseCode.equals("010")) {
            client.showSuccessMessage(message);
            client.getServerConnection().disconnectAndExit();
        }
        else {
            client.showErrorMessage("Erro ao realizar logout", message);
        }
    }
}
