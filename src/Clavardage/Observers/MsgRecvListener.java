package Clavardage.Observers;

import Clavardage.Models.Message;
import Clavardage.Views.ConversationWindow;

public class MsgRecvListener implements Listener{

    private ConversationWindow cw;

    public MsgRecvListener(ConversationWindow cw) {
        this.cw = cw;
    }

    @Override
    public void handle(Object... args) {
        Message msg = (Message) args[0];
        cw.receiveMessage(msg);
    }
}
