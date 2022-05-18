package afmiman.statsmc;

import org.bukkit.plugin.java.JavaPlugin;

public class StatsMc extends JavaPlugin{
	@Override
    public void onEnable() {
    	try {
    		afServer.main(null);
    		afServer.logMessage("Server launched on port " + afServer.serverPort);
		} catch (Exception e) {
			afServer.logMessage("Server launch failed.", true);
			e.printStackTrace();
		}
    }
    @Override
    public void onDisable() {

    }
}
