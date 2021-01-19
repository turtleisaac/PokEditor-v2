package com.turtleisaac.pokeditor.editors.scripts.gen4.commands;

import com.turtleisaac.pokeditor.framework.Buffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CommandRelocater
{
    public static void main(String[] args) throws IOException
    {
        CommandRelocater commandRelocater= new CommandRelocater("CPUE");
        commandRelocater.expandTable(1);
    }

    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private long offsetTablePointer;
    private String gameCode;
    private Buffer buffer;

    public CommandRelocater(String gameCode)
    {
        buffer= new Buffer(path + "arm9.bin");

        switch(gameCode.toLowerCase().substring(0,3))
        {
            case "cpu":
                offsetTablePointer= 0x03eaf0;
                break;

            case "apa":
                throw new RuntimeException("Game not yet supported: " + gameCode);

            case "ada":
                throw new RuntimeException("Game not yet supported: " + gameCode);

            default:
                throw new RuntimeException("Game not yet supported: " + gameCode);
        }


    }

    public void expandTable(int numAdditionalEntries) throws IOException
    {
        int originalEnd= buffer.getLength();
        buffer.skipTo(offsetTablePointer);
        long currentTablePosition= buffer.readUIntI() & 0xfffff;
        System.out.println("\nOffset Table Location: 0x" + Long.toHexString(currentTablePosition));
        buffer.skipTo(currentTablePosition);

        ArrayList<Long> commandOffsets= new ArrayList<>();
        for(int i= 0; i < 0x347; i++)
        {
            commandOffsets.add((buffer.readUIntI() & 0xfffff) + i*4);
        }

        buffer= new Buffer(path + "arm9.bin");

        for(int i= 0; i < 20; i++)
        {
            buffer.skipTo(commandOffsets.get(i));

//            System.out.println(i + " (0x" + Long.toHexString(commandOffsets.get(i)) + "): " + Arrays.toString(buffer.readTo(commandOffsets.get(i+1))));
            System.out.println(i + " (0x" + Long.toHexString(commandOffsets.get(i)) + "): " + (commandOffsets.get(i+1)-commandOffsets.get(i)));
        }

//        MemBuf offsetTable= MemBuf.create();
//        MemBuf.MemBufWriter writer= offsetTable.writer().write()
    }

    public void relocateTable(int targetOffset)
    {

    }
}
