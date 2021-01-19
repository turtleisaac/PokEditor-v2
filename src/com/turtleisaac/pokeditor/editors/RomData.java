package com.turtleisaac.pokeditor.editors;

public interface RomData
{
    String getTitle(); //0x00
    String getGameCode(); //0x0C
    String getMakerCode(); //0x10
    int getDeviceCode(); //0x12
    int getEncryptionSeed(); //0x13
    byte getDeviceCapacity(); //0x14
    byte[] getReserved(); //0x15
    byte getReserved2(); //0x1C
    int getSystemRegion(); //0x1D
    int getRomVersion(); //0x1E
    int getAutoStartFlag(); //0x1F

    int getArm9Offset(); //0x20
    int getArm9EntryAddress(); //0x24
    int getArm9LoadAddress(); //0x28
    int getArm9Length(); //0x2C

    int getArm7Offset(); //0x30
    int getArm7EntryAddress(); //0x34
    int getArm7LoadAddress(); //0x38
    int getArm7Length(); //0x3C

    int getFntbOffset(); //0x40
    int getFntbLength(); //0x44

    int getFatbOffset(); //0x48
    int getFatbLength(); //0x4C

    int getArm9OverlayOffset(); //0x50
    int getArm9OverlayLength(); //0x54

    int getArm7OverlayOffset(); //0x58
    int getArm7OverlayLength(); //0x5C

    int getNormalCardControlRegisterSettings(); //0x60
    int getSecureCardControlRegisterSettings(); //0x64

    int getIconBannerOffset(); //0x68
    int getSecureAreaCrc(); //0x6C
    short getSecureTransferTimeout(); //0x6E
    int getArm9Autoload(); //0x70
    int getArm7Autoload(); //0x74
    byte[] getSecureDisable(); //0x78

    int getRomLength();  //0x80
    int getHeaderLength(); //0x84

    byte[] getReserved3(); //0x88
    byte[] getReserved4(); //0xB0
    byte[] getNintendoLogo(); //0xC0
    short getNintendoLogoCrc(); //0x15C
    short getHeaderCrc(); //0x15E
    int getDebugRomOffset(); //0x160
    int getDebugLength(); //0x164
    int getDebugRamOffset(); //0x168
    int getReserved5(); //0x16C
    byte[] getReserved6(); //0x170
}
