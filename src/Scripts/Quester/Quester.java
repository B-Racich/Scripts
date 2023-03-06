package Scripts.Quester;

import Core.API;
import Core.Api.Common.Interfaces.ApiScript;
import Core.Api.Common.Interfaces.Quest;
import Scripts.Quester.Quests.F2P.*;
import sun.font.Script;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The basic script template
 *
 * state - can be used to setup a state based script, otherwise create a regular onLoop script within the run() method
 *
 * pause() - if setup can be used by other classes to pause the script when needed
 *
 * if tasks are supported complete the task methods
 */

public class Quester implements ApiScript {

    public final static String scriptName = "Quester";

    private API api;

    private CooksAssistant cooksAssistant;
    private WitchesPotion witchesPotion;
    private SheepShearer sheepShearer;
    private RomeoAndJuliet romeoAndJuliet;
    private RuneMysteries runeMysteries;
    private ImpCatcher impCatcher;
    private GoblinDiplomacy goblinDiplomacy;
    private RestlessGhost restlessGhost;
    private ErnestTheChicken ernestTheChicken;

    private ApiScript quest;

    private API.ScriptState scriptState;

    public Quester(API api) {
        this.api = api;

        cooksAssistant = new CooksAssistant(api);
        witchesPotion = new WitchesPotion(api);
        sheepShearer = new SheepShearer(api);
        romeoAndJuliet = new RomeoAndJuliet(api);
        runeMysteries = new RuneMysteries(api);
        impCatcher = new ImpCatcher(api);
        goblinDiplomacy = new GoblinDiplomacy(api);
        restlessGhost = new RestlessGhost(api);
        ernestTheChicken = new ErnestTheChicken(api);

//        api.antiban.idleFight = true;
    }

    enum state {}

    @Override
    public void run() {
        try {
            if(!cooksAssistant.isCompleted()) quest = cooksAssistant;
            if(!witchesPotion.isCompleted()) quest = witchesPotion;
            if(!sheepShearer.isCompleted()) quest = sheepShearer;
            if(!romeoAndJuliet.isCompleted()) quest = romeoAndJuliet;
            if(!runeMysteries.isCompleted()) quest = runeMysteries;
            if(!impCatcher.isCompleted()) quest = impCatcher;
            if(!goblinDiplomacy.isCompleted()) quest = goblinDiplomacy;
            if(!restlessGhost.isCompleted()) quest = restlessGhost;
            if(!ernestTheChicken.isCompleted()) quest = ernestTheChicken;

            quest.run();
        } catch (Exception e) {
            api.log("SCRIPT: STARTS:========");
            api.log(e.getMessage());
            api.osbot.log(e.getStackTrace());
            api.log("SCRIPT: ENDS:==========");
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void paint(Graphics2D g) {

    }

    @Override
    public API.ScriptState getState() {
        return quest.getState();
    }
}
