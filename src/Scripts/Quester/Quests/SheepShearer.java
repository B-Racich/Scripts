package Scripts.Quester.Quests;

import Core.API;
import Core.Api.Banking;
import Core.Api.Common.ApiScript;
import Core.Api.Common.Timing;
import Core.Client;
import org.osbot.rs07.api.Quests;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;

import java.awt.*;
import java.util.HashMap;

public class SheepShearer implements ApiScript {

    public final static String scriptName = "Sheep Shearer";

    private Client client;
    private API api;

    private long timeBegan;
    private long timeRan;

    public SheepShearer(Client client) {
        this.client = client;
        api = client.api;
    }

    public final int quest_id = 179;

    public boolean isCompleted() {
        if(api.mp.getQuests().isComplete(Quests.Quest.SHEEP_SHEARER)) return true;
        else return false;
    }

    private Area Freds_House = new Area(3191,3274,3189,3272);
    private Area Sheep_Pen = new Area(3195,3259,3209,3273);
    private Position Loom = new Position(3209,3213,1);

    enum state {}

    @Override
    public void run() throws NullPointerException {
        try {
            quest();
        } catch (Exception e) {
            client.log("SCRIPT: Exception: ");
            client.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean hasRoomForWool() {
        long maxInventory = 28;

        final long woolNeeded = 20;
        long woolAmount = api.mp.getInventory().getAmount("Wool");
        long woolRequired = woolNeeded - woolAmount;

        long emptySlots = api.mp.getInventory().getEmptySlots();

        boolean hasShears = api.myPlayer.hasItem("Shears");

        // Empty slots after calculating needed wool
        emptySlots =  emptySlots - woolRequired;

        if(!hasShears) emptySlots--;

        client.log("Empty slots: "+emptySlots+"\tWool Required: "+woolRequired);
        if(emptySlots >= 0) return true;
        else return false;
    }

    private boolean hasWoolOrBall() {
        long woolAmt = api.mp.getInventory().getAmount("Wool");
        long ballAmt = api.mp.getInventory().getAmount("Ball of wool");

        client.log("Wool+Balls: "+(woolAmt+ballAmt));
        if(woolAmt+ballAmt >= 20) return true;
        else return false;
    }

    private void quest() {
        int quest_state = api.mp.getConfigs().get(quest_id);

        switch(quest_state) {
            case 0:
                if(hasWoolOrBall()) {
                    if(api.mp.getInventory().getAmount("Ball of wool") < 20) {
                        if(api.myPlayer.isWithin(Loom, 5)) {
                            api.interact.interactOb("Spinning wheel", "Spin");
                            Timing.waitCondition(() -> api.mp.getWidgets().isVisible(270,14), 3000);
                            if(api.mp.getWidgets().isVisible(270,14)) {
                                api.mp.getWidgets().interact(270,14, "Spin");
                                Timing.waitCondition(() -> !api.mp.getInventory().contains("Wool") && !api.myPlayer.isBusy(false), 60000);
                            }
                        } else api.myPlayer.moveTo(Loom);
                    }
                    else if(api.mp.getInventory().getAmount("Ball of wool") >= 20) {
                        if(Freds_House.contains(api.mp.myPlayer())) {
                            api.interact.talkNPC("Fred the Farmer", new int[]{1});
                        } else api.myPlayer.moveTo(Freds_House);
                    }
                }
                else if(hasRoomForWool()) {
                    if(api.myPlayer.hasItem("Shears")) {
                        if(Sheep_Pen.contains(api.mp.myPlayer())) {
                            if(api.mp.getInventory().getAmount("Wool")<20) {
                                api.interact.interactNpc("Sheep", "Shear");
                            }
                        } else api.myPlayer.moveTo(Sheep_Pen);
                    } else {
                        client.log("Go to freds");
                        if(Freds_House.contains(api.mp.myPlayer())) {
                            api.interact.pickUpItem("Shears");
                        } else api.myPlayer.moveTo(Freds_House);
                    }
                } else {
                    if(Banks.LUMBRIDGE_UPPER.contains(api.mp.myPlayer())) {
                        if(api.banking.open()) {
                            HashMap withdraw = new HashMap<String, Integer>(){{put("Shears",1);}};
                            api.banking.bank(null, withdraw, Banking.methods.DEPOSIT_ALL_WITHDRAW);
                        }
                    }
                }
            case 1:
            case 21:
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void paint(Graphics2D g) {

    }

    @Override
    public void setTask(HashMap<String, Integer> tasks) {

    }

    @Override
    public boolean hasTask() {
        return false;
    }

    @Override
    public boolean completedTask() {
        return false;
    }

    @Override
    public <state> state getState() {
        return null;
    }
}
