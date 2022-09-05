import java.io.File;

public class HeartGoldTest extends GameTest
{
    // assigning the values
    protected void setUp() throws Exception {
        game = "HeartGold";
        gameCode = "IPKE";
        romPath = "roms/HeartGold.nds";

        personalFilePath = "a" + File.separator + "0" + File.separator + "0" + File.separator + "2";
        personalDirName = "personal";

        movesFilePath = "a" + File.separator + "0" + File.separator + "1" + File.separator + "1";
        movesDirName = "waza_tbl";

        learnsetsFilePath = "a" + File.separator + "0" + File.separator + "3" + File.separator + "3";
        learnsetsDirName = "wotbl";

        itemsFilePath = "a" + File.separator + "0" + File.separator + "1" + File.separator + "7";
        itemsDirName = "item_data";

        trainersFilePath = "a" + File.separator + "0" + File.separator + "5" + File.separator + "5";
        trainersDirName = "trdata";
        trainerTeamsFilePath = "a" + File.separator + "0" + File.separator + "5" + File.separator + "6";
        trainerTeamsDirName = "trpoke";

        super.setUp();
    }
}
