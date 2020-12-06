package Clavardage.Observers;

import Clavardage.Models.Message;
import Clavardage.Views.ConversationPanel;

public class MsgRecvListener implements Listener{

    private final ConversationPanel conversationPanel;

    public MsgRecvListener(ConversationPanel conversationPanel) {
        this.conversationPanel = conversationPanel;
    }

    @Override
    public void handle(Object... args) {
        Message msg = (Message) args[0];
        this.conversationPanel.receiveMessage(msg);
    }
}
