import com.turtleisaac.pokeditor.project.Project;

import java.io.File;
import java.io.IOException;

public class PlatinumTest extends GameTest
{
    // assigning the values
    protected void setUp() throws Exception {
        game = "Plat";
        gameCode = "CPUE";
        romPath = "Platinum.nds";

        personalFilePath = "poketool" + File.separator + "personal" + File.separator + "pl_personal.narc";
        personalDirName = "pl_personal";

        movesFilePath = "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl.narc";
        movesDirName = "pl_waza_tbl";

        learnsetsFilePath = "poketool" + File.separator + "personal" + File.separator + "wotbl.narc";
        learnsetsDirName = "wotbl";

        itemsFilePath = "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data.narc";
        itemsDirName = "pl_item_data";

        trainersFilePath = "poketool" + File.separator + "trainer" + File.separator + "trdata.narc";
        trainersDirName = "trdata";
        trainerTeamsFilePath = "poketool" + File.separator + "trainer" + File.separator + "trpoke.narc";
        trainerTeamsDirName = "trpoke";

        super.setUp();
    }
}
