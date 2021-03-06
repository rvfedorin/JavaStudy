/*
 * Created by Roman V. Fedorin
 */
package actions;

import gui.MainFrame;
import static gui.ProgressBar.progressBar;
import tools.Config;
import static tools.Config.SSH_LOGIN;
import static tools.Config.SSH_PASS_L;
import static tools.Config.WORK;
import static tools.CryptDecrypt.getEncrypt;
import tools.SSH;

/**
 *
 * @author Wolf
 */
public class StopSession implements Runnable {

    private final String mnemokod;
    private final String ipISG;
    private final char[] key;
    private MainFrame parent;
    private SSH ssh;

    public StopSession(String mnemokod, String ipISG, char[] key, MainFrame parent) {
        this.mnemokod = mnemokod;
        this.ipISG = ipISG;
        this.key = key;
        this.parent = parent;

    }

    @Override
    public void run() {
        progressBar.setIndeterminate(true);
        String stopSessionCommand = "clear subscriber session username " + mnemokod;
        try {
            ssh = new SSH(getEncrypt(new String(key), WORK), key);
            String[] commands = new String[3];
            commands[0] = "en";
            commands[1] = getEncrypt(new String(key), Config.TO_CLOSE);
            commands[2] = stopSessionCommand;

            String response = ssh.telnetConnection(commands,
                    getEncrypt(new String(key), SSH_LOGIN),
                    getEncrypt(new String(key), SSH_PASS_L),
                    ipISG);

            String result = "Отправлено: \n\t" + stopSessionCommand;
            result += "\n\tНа ISG: " + ipISG;
//            System.out.println(result);
            parent.resultFrame.showResult(result);

        } catch (Exception ex) {
            ex.printStackTrace();
            ShowDialogs.info("Ошибка подключения к ISG. StopSession->run()");
        } finally {
            progressBar.setIndeterminate(false);
            ssh.closeSession();
        }
    }
}
