package com.turtleisaac.pokeditor.framework;

public class CRC16
{
    public static int calculateCrc(byte... arr)
    {
        CRC16 crc16= new CRC16();

        for(byte b : arr)
        {
            crc16.update(b);
        }

        return crc16.getValue();
    }

    private int value = 0;

    public CRC16() {
    }

    public void update(byte var1) {
        int var2 = var1;

        for(int var4 = 7; var4 >= 0; --var4) {
            var2 <<= 1;
            int var3 = var2 >>> 8 & 1;
            if ((this.value & '耀') != 0) {
                this.value = (this.value << 1) + var3 ^ 4129;
            } else {
                this.value = (this.value << 1) + var3;
            }
        }

        this.value &= 65535;
    }

    public int getValue()
    {
        return value;
    }

    public void reset() {
        this.value = 0;
    }
}
