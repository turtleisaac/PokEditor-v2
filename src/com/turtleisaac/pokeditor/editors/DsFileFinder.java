package com.turtleisaac.pokeditor.editors;

import com.turtleisaac.pokeditor.framework.BLZCoder;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import sun.misc.BASE64Encoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DsFileFinder
{

    public static void main(String[] args) throws Exception
    {
        DsFileFinder reader= new DsFileFinder();
        reader.readRom(args);
    }

    private String path= System.getProperty("user.dir") + File.separator;
    private String[] romCapacities= new String[13];
    private String rom;
    private String tempPath= "temp" + File.separator;
    private String tempPathUnpack= tempPath;
    private Buffer buffer;
    private RomData romData;
    private ArrayList<FimgEntry> fimgEntries;
    private ArrayList<FimgEntry> sortedEntries;
    private int fileOffset;
    private int length;
    private int fileID;
    private int newFileLength;
    private String type;

    private HashMap<String,String> map1 = new HashMap<>();
    private HashMap<String,String> map2 = new HashMap<>();
    private boolean rom2= true;
    private String rom2Name;
    private String rom1Name;

    public DsFileFinder()
    {
        Arrays.fill(romCapacities,"");
        romCapacities[6]= "8MB";
        romCapacities[7]= "16MB";
        romCapacities[8]= "32MB";
        romCapacities[9]= "64MB";
        romCapacities[10]= "128MB";
        romCapacities[11]= "256MB";
        romCapacities[12]= "512MB";
    }

    public void readRom(String[] args) throws Exception
    {
//        String rom2= args[args.length-1];
//        String rom1= args[args.length-2];
//        this.rom= path + rom2;
//        buffer= new Buffer(rom2);
//        readHeader();
//        readFatb();
//        map1= getAllFiles();
//        this.rom2= false;
//        fimgEntries= new ArrayList<>();
//
//        this.rom= path + rom1;
//        buffer= new Buffer(rom1);
//        readHeader();
//        readFatb();
//        map2= getAllFiles();
//
//        compareAllFiles();

        String rom= args[args.length-1];
        this.rom= path + rom;
        buffer= new Buffer(rom);
        readHeader();
        readFatb();
        /**
         * DP Overlays: 0x00 - 0x56
         * Pt Overlays: 0x00 - 0x7A
         * HGSS Overlays: 0x00 - 0x80
         */
//        findFile(64);
//        findFile(0x7A,(byte)0xAB,(byte)0x01,(byte)0x00,(byte)0x00);
//        findFileBytes(0x7A);
//        findFile((byte)0x83,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x86,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x89,(byte)0x01);
        System.out.print(romData.getTitle() + ": ");

//        readArm9();

//
//        findFile(0x80, 0x11, 0x00, 0x00, 0x00, 0x2f, 0x00, 0x00, 0x00);
//
//        findFile(0x80, 0x2f, 0x00, 0x00, 0x00, 0x11, 0x00, 0x00, 0x00);
//
//        findFile(0x80, 0x1f, 0x03, 0x00, 0x00);

        findFile(0x7A, 0x14,0x47,0x0F,0x02);
        findFile(0x7A, 0x02,0x0F,0x47,0x14);
//        findFile(0x7A, 0x84, 0x03);


//        writeFile(5);
    }

    public void readHeader() throws IOException
    {
        String title= buffer.readString(12).trim();
        if(rom2)
        {
            rom2Name= title;
        }
        else
        {
            rom1Name= title;
        }
        String gameCode= buffer.readString(4);
        String makerCode= buffer.readString(2);
        int deviceCode= buffer.readByte();
        int encryptionSeed= buffer.readByte();
        byte romChipCapacity= buffer.readBytes(1)[0];
        byte[] reserved1= buffer.readBytes(7);
        byte reserved2= (byte)buffer.readByte();
        int systemRegion= buffer.readByte();
        int romVersion= buffer.readByte();
        int autoStartFlag= buffer.readByte();

        int arm9Offset= buffer.readInt();
        if(arm9Offset < 0x4000) {
            throw new RuntimeException("Invalid ROM Header: ARM9 Offset");
        }
        int arm9EntryAddress= buffer.readInt();
        if(!(arm9EntryAddress >= 0x2000000 && arm9EntryAddress <= 0x23BFE00)) {
            throw new RuntimeException("Invalid ROM Header: ARM9 Entry Address");
        }
        int arm9LoadAddress= buffer.readInt();
        if(!(arm9LoadAddress >= 0x2000000 && arm9LoadAddress <= 0x23BFE00)) {
            throw new RuntimeException("Invalid ROM Header: ARM9 RAM Address");
        }
        int arm9Length= buffer.readInt();
        if(arm9Length > 0x3BFE00) {
            throw new RuntimeException("Invalid ROM Header: ARM9 Size");
        }
        int arm7Offset= buffer.readInt();
        if(arm7Offset < 0x8000) {
            throw new RuntimeException("Invalid ROM Header: ARM7 Offset");
        }
        int arm7EntryAddress= buffer.readInt();
        if(!((arm7EntryAddress >= 0x2000000 && arm7EntryAddress <= 0x23BFE00) || (arm7EntryAddress >= 0x37F8000 && arm7EntryAddress <= 0x3807E00))) {
            throw new RuntimeException("Invalid ROM Header: ARM7 Entry Address");
        }
        int arm7LoadAddress= buffer.readInt();
        if(!((arm7LoadAddress >= 0x2000000 && arm7LoadAddress <= 0x23BFE00) || (arm7LoadAddress >= 0x37F8000 && arm7LoadAddress <= 0x3807E00))) {
            throw new RuntimeException("Invalid ROM Header: ARM7 Load Address");
        }
        int arm7Length= buffer.readInt();
        if(arm7Length > 0x3BFE0) {
            throw new RuntimeException("Invalid ROM Header: ARM7 Size");
        }

        int fntbOffset= buffer.readInt();
        int fntbLength= buffer.readInt();

        int fatbOffset= buffer.readInt();
        int fatbLength= buffer.readInt();

        int arm9OverlayOffset= buffer.readInt();
        int amr9OverlayLength= buffer.readInt();

        int arm7OverlayOffset= buffer.readInt();
        int arm7OverlayLength= buffer.readInt();

        int normalCardControlRegisterSettings= buffer.readInt();
        int secureCardControlRegisterSettings= buffer.readInt();

        int iconBannerOffset= buffer.readInt();
        short secureAreaCrc= buffer.readShort();
        short secureTransferTimeout= buffer.readShort();
        int arm9Autoload= buffer.readInt();
        int arm7Autoload= buffer.readInt();
        byte[] secureDisable= buffer.readBytes(8);

        int romLength= buffer.readInt();
        int headerLength= buffer.readInt();

        byte[] reserved3= buffer.readBytes(212);
        byte[] reserved4= buffer.readBytes(16);
        byte[] nintendoLogo= buffer.readBytes(0x156);
        short nintendoLogoCrc= buffer.readShort();
        short headerCrc= buffer.readShort();
        int debugRomOffset= buffer.readInt();
        int debugLength= buffer.readInt();
        int debugRamOffset= buffer.readInt();
        int reserved5= buffer.readInt();
        byte[] reserved6= buffer.readBytes(144);

        romData= new RomData() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getGameCode() {
                return gameCode;
            }

            @Override
            public String getMakerCode() {
                return makerCode;
            }

            @Override
            public int getDeviceCode() {
                return deviceCode;
            }

            @Override
            public int getEncryptionSeed() {
                return encryptionSeed;
            }

            @Override
            public byte getDeviceCapacity() {
                return romChipCapacity;
            }

            @Override
            public byte[] getReserved() {
                return reserved1;
            }

            @Override
            public byte getReserved2() {
                return reserved2;
            }

            @Override
            public int getSystemRegion() {
                return systemRegion;
            }

            @Override
            public int getRomVersion() {
                return romVersion;
            }

            @Override
            public int getAutoStartFlag() {
                return autoStartFlag;
            }

            @Override
            public int getArm9Offset() {
                return arm9Offset;
            }

            @Override
            public int getArm9EntryAddress() {
                return arm9EntryAddress;
            }

            @Override
            public int getArm9LoadAddress() {
                return arm9LoadAddress;
            }

            @Override
            public int getArm9Length() {
                return arm9Length;
            }

            @Override
            public int getArm7Offset() {
                return arm7Offset;
            }

            @Override
            public int getArm7EntryAddress() {
                return arm7EntryAddress;
            }

            @Override
            public int getArm7LoadAddress() {
                return arm7LoadAddress;
            }

            @Override
            public int getArm7Length() {
                return arm7Length;
            }

            @Override
            public int getFntbOffset() {
                return fntbOffset;
            }

            @Override
            public int getFntbLength() {
                return fntbLength;
            }

            @Override
            public int getFatbOffset() {
                return fatbOffset;
            }

            @Override
            public int getFatbLength() {
                return fatbLength;
            }

            @Override
            public int getArm9OverlayOffset() {
                return arm9OverlayOffset;
            }

            @Override
            public int getArm9OverlayLength() {
                return amr9OverlayLength;
            }

            @Override
            public int getArm7OverlayOffset() {
                return arm7OverlayOffset;
            }

            @Override
            public int getArm7OverlayLength() {
                return arm7OverlayLength;
            }

            @Override
            public int getNormalCardControlRegisterSettings() {
                return normalCardControlRegisterSettings;
            }

            @Override
            public int getSecureCardControlRegisterSettings() {
                return secureCardControlRegisterSettings;
            }

            @Override
            public int getIconBannerOffset() {
                return iconBannerOffset;
            }

            @Override
            public int getSecureAreaCrc() {
                return secureAreaCrc;
            }

            @Override
            public short getSecureTransferTimeout() {
                return secureTransferTimeout;
            }

            @Override
            public int getArm9Autoload() {
                return arm9Autoload;
            }

            @Override
            public int getArm7Autoload() {
                return arm7Autoload;
            }

            @Override
            public byte[] getSecureDisable() {
                return secureDisable;
            }

            @Override
            public int getRomLength() {
                return romLength;
            }

            @Override
            public int getHeaderLength() {
                return headerLength;
            }

            @Override
            public byte[] getReserved3() {
                return reserved3;
            }

            @Override
            public byte[] getReserved4() {
                return reserved4;
            }

            @Override
            public byte[] getNintendoLogo() {
                return nintendoLogo;
            }

            @Override
            public short getNintendoLogoCrc() {
                return nintendoLogoCrc;
            }

            @Override
            public short getHeaderCrc() {
                return headerCrc;
            }

            @Override
            public int getDebugRomOffset() {
                return debugRomOffset;
            }

            @Override
            public int getDebugLength() {
                return debugLength;
            }

            @Override
            public int getDebugRamOffset() {
                return debugRamOffset;
            }

            @Override
            public int getReserved5() {
                return reserved5;
            }

            @Override
            public byte[] getReserved6() {
                return reserved6;
            }
        };

        System.out.println("Title: " + romData.getTitle());
        System.out.println("Game Code: " + romData.getGameCode());
        System.out.println("Maker Code: " + romData.getMakerCode());
        System.out.println("Device Code: " + romData.getDeviceCode());
        System.out.println("Encryption Seed: " + romData.getEncryptionSeed());
        System.out.println("File Length: " + romCapacities[romData.getDeviceCapacity()]);
        System.out.println("Reserved 1: " + Arrays.toString(romData.getReserved()));
        System.out.println("Reserved 2: " + romData.getReserved2());
        System.out.println("System Region: " + romData.getSystemRegion());
        System.out.println("Rom Version: " + romData.getRomVersion());
        System.out.println("Internal Flags: " + romData.getAutoStartFlag());
        System.out.println("Arm9 Offset: " + romData.getArm9Offset());
        System.out.println("Arm9 Entry Address: " + romData.getArm9EntryAddress());
        System.out.println("Arm9 Load Address: " + romData.getArm9LoadAddress());
        System.out.println("Arm9 Length: " + romData.getArm9Length());
        System.out.println("Arm7 Offset: " + romData.getArm7Offset());
        System.out.println("Arm7 Entry Address: " + romData.getArm7EntryAddress());
        System.out.println("Arm7 Load Address" + romData.getArm7LoadAddress());
        System.out.println("Arm7 Length: " + romData.getArm7Length());
        System.out.println("Fntb Offset: " + romData.getFntbOffset());
        System.out.println("Fntb Length: " + romData.getFntbLength());
        System.out.println("Fatb Offset: " + romData.getFatbOffset());
        System.out.println("Fatb Length: " + romData.getFatbLength());
        System.out.println("Arm9 Overlay Offset: " + romData.getArm9OverlayOffset());
        System.out.println("Arm9 Overlay Length: " + romData.getArm9OverlayLength());
        System.out.println("Arm7 Overlay Offset: " + romData.getArm7OverlayOffset());
        System.out.println("Arm7 Overlay Length: " + romData.getArm7OverlayLength());
        System.out.println("Normal Card Control Register Settings: " + romData.getNormalCardControlRegisterSettings());
        System.out.println("Secure Card Control Register Settings: " + romData.getSecureCardControlRegisterSettings());
        System.out.println("Icon Banner Offset: " + romData.getIconBannerOffset());
        System.out.println("Secure Area Crc: " + romData.getSecureAreaCrc());
        System.out.println("Secure Transfer Timeout: " + romData.getSecureTransferTimeout());
        System.out.println("Arm9 Autoload: " + romData.getArm9Autoload());
        System.out.println("Arm7 Autoload: " + romData.getArm7Autoload());
        System.out.println("Secure Disable: " + Arrays.toString(romData.getSecureDisable()));
        System.out.println("Rom Length: " + romData.getRomLength());
        System.out.println("Header Length: " + romData.getHeaderLength());
        System.out.println("Reserved 3: " + Arrays.toString(romData.getReserved3()));
        System.out.println("Reserved 4: " + Arrays.toString(romData.getReserved4()));
        System.out.println("Nintendo Logo: " + Arrays.toString(romData.getNintendoLogo()));
        System.out.println("Nintendo Logo Crc: " + romData.getNintendoLogoCrc());
        System.out.println("Header Crc: " + romData.getHeaderCrc());
        System.out.println("Debug Rom Offset: " + romData.getDebugRomOffset());
        System.out.println("Debug Length: " + romData.getDebugLength());
        System.out.println("Debug Ram Offset: " + romData.getDebugRamOffset());
        System.out.println("Reserved 5: " + romData.getReserved5());
        System.out.println("Reserved 6: " + Arrays.toString(romData.getReserved6()));

        System.out.println("End of header: " + buffer.getPosition() + "\n");
    }

    public void readArm9() throws IOException
    {
        buffer= new Buffer(rom);
        buffer.skipTo(romData.getArm9Offset());
        BinaryWriter writer= new BinaryWriter("arm9.bin");
        writer.write(buffer.readBytes(romData.getArm9Length()));
        writer.close();
    }


    private int SS_HG_FIRST_FILE= 0x81;
    private int D_P_PT_FIRST_FILE= 0x7A;

    public void readFatb()
    {
        int firstFileID= 0;
        switch (romData.getTitle())
        {
            case "POKEMON SS" :

            case "POKEMON HG" :
                firstFileID= SS_HG_FIRST_FILE;
                break;

            case "POKEMON D" :

            case "POKEMON P" :

            case "POKEMON PL" :
                firstFileID= D_P_PT_FIRST_FILE;
                break;
        }
//        System.out.println("First file ID: " + firstFileID + "\n");

        int fatbPos= 0;
        buffer.skipTo(romData.getFatbOffset());
//        System.out.println(buffer.getPosition() + "\n");
        fimgEntries= new ArrayList<>();
//        System.out.println("Length: " + romData.getFatbLength()/8 + " files");
        int lastEnd= 0;


        for(int i= 0; i < romData.getFatbLength()/8; i++)
        {
//            System.out.println("Fatb Offset: " + buffer.getPosition());
            long startingOffset= buffer.readUIntI();
            long endingOffset= buffer.readUIntI();
            fatbPos+= 4;
//            System.out.println("ID: 0x" + Integer.toHexString(i));
//            System.out.println("Starting Offset: " +startingOffset);
//            System.out.println("Ending Offset: " + endingOffset);
//            System.out.println("Length: " + (endingOffset-startingOffset) + "\n");

            long gap= 0;
            if(i > firstFileID)
            {
                gap= startingOffset-lastEnd;
            }

            if(0xF4714 > startingOffset && 0xf4714 < endingOffset)
                System.out.println("File " + i);

            int finalI = i;
            long finalDiff = gap;
            fimgEntries.add(new FimgEntry() {
                @Override
                public int getId() {
                    return finalI;
                }

                @Override
                public long getStartingOffset() {
                    return startingOffset;
                }

                @Override
                public long getEndingOffset() {
                    return endingOffset;
                }

                @Override
                public long getGap() {
                    return finalDiff;
                }
            });
            lastEnd= (int) endingOffset;
        }
        System.out.println("Number of recorded entries: " + fimgEntries.size() + "\n");


    }

    public void findNarc(String[] args) throws IOException
    {
        int toFind= Integer.parseInt(args[0]);
        System.out.println(toFind);
        Buffer romBuffer;
        FimgEntry fimgEntry;

        for(int i= 0; i < fimgEntries.size(); i++)
        {
            fimgEntry= fimgEntries.get(i);
            romBuffer= new Buffer(rom);
            romBuffer.skipTo((int)fimgEntry.getStartingOffset());
            String magic= romBuffer.readString(4);
            if(magic.equals("NARC"))
            {
                System.out.println("Narc\n");
                romBuffer.skipBytes(20);
                int numFiles= romBuffer.readInt();
                if(numFiles == toFind || numFiles == toFind-1 || numFiles == toFind+1)
                {
                    System.out.print("Current file: " + i + ", ");
                    System.out.println("numFiles: " + numFiles);
                    System.out.println("File length: " + (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()) + "\n");

                }
            }
            romBuffer.close();
        }
    }

    public void writeFile(int id) throws IOException
    {
        FimgEntry fimgEntry= fimgEntries.get(id);
        buffer= new Buffer(rom);
        buffer.skipTo((int) fimgEntry.getStartingOffset());
        BinaryWriter writer= new BinaryWriter("Found" + File.separator + romData.getGameCode() + "_Overlay_" + id + "_Original.bin");
        writer.write(buffer.readBytes((int) (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset())));
        writer.close();
    }

    public void findFileBytes(int max) throws IOException
    {
        Buffer toFind = new Buffer(path + "toFind.bin");
        byte[] bytes= toFind.readRemainder();
        Buffer romBuffer;
        FimgEntry fimgEntry;

        for (int i = 0; i < max; i++)
        {
            fimgEntry = fimgEntries.get(i);
            romBuffer = new Buffer(rom);
            romBuffer.skipTo((int) fimgEntry.getStartingOffset());
            byte[] contents = romBuffer.readBytes((int) (fimgEntry.getEndingOffset() - fimgEntry.getStartingOffset()));
            byte[] arr;
            for (int j = 0; j < contents.length; j++)
            {
                arr = Arrays.copyOfRange(contents, j, j + bytes.length);
                if (Arrays.equals(arr, bytes))
                {
                    System.out.print("Current file: " + i + ", ");
                    System.out.println("Global offset: 0x" + Integer.toHexString((int) (fimgEntry.getStartingOffset() + j)));
                    System.out.println("Offset in file: 0x" + Integer.toHexString(j));
                    System.out.println("File length: " + (fimgEntry.getEndingOffset() - fimgEntry.getStartingOffset()) + "\n");
                }
            }


            romBuffer.close();
            toFind.close();
        }
    }

    public void findFile(int max, String bytes) throws IOException
    {
//        findFile(max, Arrays.stream(bytes.split(" ")).map(s -> Integer.parseInt(s, 16)).mapToInt(Integer::intValue).toArray());
        String[] sArr= bytes.split(" ");
        int[] arr= new int[sArr.length];
        for(int i= 0; i < arr.length; i++)
        {
            arr[i]= Integer.parseInt(sArr[i],16);
        }
        System.out.println(Arrays.toString(arr));
        findFile(max,arr);
    }

    public void findFile(int max, int... bytes) throws IOException
    {
        int idx= 0;
        StringBuilder str= new StringBuilder("[");
        byte[] byteArr= new byte[bytes.length];
        for(int i= 0; i < bytes.length; i++)
        {
            byteArr[i]= (byte)bytes[i];
            str.append("0x").append(Integer.toHexString(bytes[i])).append(",");
        }
        String toString= str.substring(0,str.length()-1) + "]";
        System.out.println(toString + "\n");

        Buffer romBuffer;
        FimgEntry fimgEntry;
        boolean contains= false;
        BinaryWriter writer;

        for(int i= 0; i < max; i++)
        {
            fimgEntry= fimgEntries.get(i);
            romBuffer= new Buffer(rom);
            romBuffer.skipTo((int)fimgEntry.getStartingOffset());
            byte[] contents= romBuffer.readBytes((int) (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()));
            byte[] contentsCopy= Arrays.copyOf(contents,contents.length);

            if(romData.getGameCode().equalsIgnoreCase("ipke") || romData.getGameCode().equalsIgnoreCase("ipge"))
            {
                try
                {
                    contents= new BLZCoder(null).BLZ_DecodePub(contents,"overlay");
                }
                catch (RuntimeException e)
                {
                    contents= contentsCopy;
                }
            }



            byte[] arr;
            for(int j= 0; j < contents.length; j++)
            {
                arr= Arrays.copyOfRange(contents,j,j+bytes.length);
                if(Arrays.equals(arr,byteArr))
                {
                    contains= true;
                    System.out.print("Current file: " + i + " (0x" +Integer.toHexString(i) + "), ");
                    System.out.println("Global offset: 0x" + Integer.toHexString((int) (fimgEntry.getStartingOffset() + j)));
                    System.out.println("Offset in file: 0x" + Integer.toHexString(j));
                    System.out.println("File length: " + (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()) + "\n");
                    idx++;
                }
            }

            if(contains)
            {
                String folderPath= path + "Found" + File.separator + romData.getGameCode() + " " + toString;
                new File(folderPath).mkdirs();

                writer= new BinaryWriter(  folderPath + File.separator + "Overlay9_" + i + ".bin");
                writer.write(contents);
                writer.close();
                contains=  false;
            }

            romBuffer.close();
        }
        System.out.println("\nTotal number of instances found: " + idx + "\n");
    }

    public void findFile(boolean decompress, int max, int... intArr) throws IOException
    {
        byte[] bytes= new byte[intArr.length];
        for(int i= 0; i < intArr.length; i++)
        {
            bytes[i]= (byte) intArr[i];
        }

        clearDirectory(new File("Found"));
        new File("Found").mkdir();

        Buffer romBuffer;
        BinaryWriter writer;
        FimgEntry fimgEntry;

        for(int i= 0; i < fimgEntries.size(); i++)
        {
//            System.out.println("File: " + i);

            fimgEntry= fimgEntries.get(i);
            romBuffer= new Buffer(rom);
            romBuffer.skipTo((int)fimgEntry.getStartingOffset());
            byte[] fileContents= romBuffer.readBytes((int) (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()));


            if(decompress)
            {
                BLZCoder blzCoder= new BLZCoder(null);
                fileContents= blzCoder.BLZ_DecodePub(fileContents,"");
            }
            byte[] arr;
            for(int j= 0; j < fileContents.length; j++)
            {
                arr= Arrays.copyOfRange(fileContents,j,j+bytes.length);
                if(Arrays.equals(arr,bytes))
                {
                    writer= new BinaryWriter("Found" + File.separator + romData.getGameCode() + "_Overlay_" + i + "_Decompressed.bin");
                    System.out.print("\nCurrent file: " + i + ", ");
                    System.out.println("Global offset: 0x" + Integer.toHexString((int) (fimgEntry.getStartingOffset() + j)));
                    System.out.println("Offset in file: 0x" + Integer.toHexString(j));
                    System.out.println("File length: " + (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()) + "\n");
                    writer.write(fileContents);
                    writer.close();
                    writeFile(i);
                }
            }


            romBuffer.close();
        }
    }


    public HashMap<String,String> getAllFiles() throws IOException, NoSuchAlgorithmException
    {
        Buffer romBuffer;
        FimgEntry fimgEntry;
        HashMap<String,String> map= new HashMap<>();

        for(int i= 0; i < fimgEntries.size(); i++)
        {
            fimgEntry= fimgEntries.get(i);
            romBuffer= new Buffer(rom);
            romBuffer.skipTo((int)fimgEntry.getStartingOffset());
            System.out.println(romData.getTitle() + " File: 0x" + Integer.toHexString(i));

            map.put(getHash(romBuffer.readBytes((int) (fimgEntry.getEndingOffset()-fimgEntry.getStartingOffset()))),"0x" + Integer.toHexString(i));

            romBuffer.close();
        }
        System.out.println("\n-------------------------------\n");
        return map;
    }

    public String getHash(byte[] contents) throws NoSuchAlgorithmException
    {
        MessageDigest digest= MessageDigest.getInstance("SHA-256");
        digest.update(contents);

        return new BASE64Encoder().encode(digest.digest());
    }


    public void compareAllFiles() throws IOException
    {
        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "File Comparator Log.txt"));
        int numIdentical= 0;

        for(String file1 : map1.keySet())
        {
            System.out.println(rom1Name + " file with ID " + map1.get(file1) + " is identical to: " + rom2Name +  " file with ID:");
            writer.write(rom1Name + " file with ID " + map1.get(file1) + " is identical to: " + rom2Name + " file with ID:\n");
            if(map2.containsKey(file1))
            {
                System.out.println("  " + map2.get(file1));
                writer.write("  " + map2.get(file1));
                numIdentical++;
            }
            System.out.println();
            writer.write("\n\n");
            writer.flush();
        }
        System.out.println("Number of identical files between " + rom1Name + " and " + rom2Name + ": " + numIdentical);
        writer.write("Number of identical files between " + rom1Name + " and " + rom2Name + ": " + numIdentical + "\n");
    }

    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(DsFileFinder::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private void clearDirectory(File directory)
    {
        for(File subfile : Objects.requireNonNull(directory.listFiles()))
        {
            if(subfile.isDirectory())
            {
                clearDirectory(subfile);
            }
            else
            {
                subfile.delete();
            }
        }
        directory.delete();
    }



    private boolean contains(byte[] arr, byte val)
    {
        for(byte num : arr)
        {
            if(num == val)
            {
                return true;
            }
        }
        return false;
    }
}

