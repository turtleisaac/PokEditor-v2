package com.turtleisaac.pokeditor.utilities.images.ncer;

import com.sun.glass.ui.Size;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.utilities.images.ImageActions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NcerReader
{
    public static NcerData readFile(String file)
    {
        Buffer buffer= new Buffer(file);

        //Header
        String headerID= buffer.readString(4);
        int endianness= buffer.readUInt16();
        int constant= buffer.readUInt16();
        long fileSize= buffer.readUInt32();
        int headerSize= buffer.readUInt16();
        int numSections= buffer.readUInt16();

        //Cell Bank
        String cellBankID= buffer.readString(4);
        long cellBankSectionSize= buffer.readUInt32();
        int numBanks= buffer.readUInt16();
        int bankType= buffer.readUInt16();
        long bankDataOffset= buffer.readUInt32();
        long blockSize= buffer.readUInt32() & 0xff;
        long partitionDataOffset= buffer.readUInt32();
        long unused= buffer.readLong();
        NcerData.Bank[] banks= new NcerData.Bank[numBanks];

        long maxPartitionSize= 0;
        long firstPartitionDataOffset= 0;
        if(partitionDataOffset != 0)
        {
            buffer.skipTo(headerSize + partitionDataOffset + 8);
            maxPartitionSize= buffer.readUInt32();
            firstPartitionDataOffset= buffer.readUInt32();
            buffer.skipBytes((int) (firstPartitionDataOffset - 8));
            for(int i= 0; i < numBanks; i++)
            {
                long partitionOffset= buffer.readUInt32();
                long partitionSize= buffer.readUInt32();

                banks[i]= new NcerData.Bank()
                {
                    @Override
                    public int getNumCells()
                    {
                        return 0;
                    }

                    @Override
                    public int getReadOnlyCellInfo()
                    {
                        return 0;
                    }

                    @Override
                    public long getCellOffset()
                    {
                        return 0;
                    }

                    @Override
                    public long getPartitionOffset()
                    {
                        return partitionOffset;
                    }

                    @Override
                    public long getPartitionSize()
                    {
                        return partitionSize;
                    }

                    @Override
                    public NcerData.OAM[] getOAMs()
                    {
                        return null;
                    }

                    @Override
                    public short getXMax()
                    {
                        return 0;
                    }

                    @Override
                    public short getYMax()
                    {
                        return 0;
                    }

                    @Override
                    public short getXMin()
                    {
                        return 0;
                    }

                    @Override
                    public short getYMin()
                    {
                        return 0;
                    }
                };
            }
        }

        buffer= new Buffer(file);
        buffer.skipTo(headerSize + bankDataOffset + 8);

            //Read banks
        for(int i= 0; i < numBanks; i++)
        {
            int cellNumber= buffer.readUInt16();
            int readOnlyCellInfo= buffer.readUInt16();
            long cellOffset= buffer.readUInt32();

            long partitionOffset= banks[i].getPartitionOffset();
            long partitionSize= banks[i].getPartitionSize();

            short xMax= bankType == 1 ? buffer.readShort() : 0;
            short yMax= bankType == 1 ? buffer.readShort() : 0;
            short xMin= bankType == 1 ? buffer.readShort() : 0;
            short yMin= bankType == 1 ? buffer.readShort() : 0;

            long position= buffer.getPosition() & 0xffffffffL;

            if(bankType == 0)
                buffer.skipTo(buffer.getPosition() + (numBanks - (i+1)) * 8 + cellOffset);
            else
                buffer.skipTo(buffer.getPosition() + (numBanks - (i+1)) * 0x10 + cellOffset);

            NcerData.OAM[] oams= new NcerData.OAM[cellNumber];

                //Read cells
            for(int x= 0; x < cellNumber; x++)
            {
                int obj0Value= buffer.readUInt16();
                int obj1Value= buffer.readUInt16();
                int obj2Value= buffer.readUInt16();

                NcerData.Obj0 obj0= getObj0(obj0Value);
                NcerData.Obj1 obj1= getObj1(obj1Value,obj0.getRsFlag());
                NcerData.Obj2 obj2= getObj2(obj2Value);

                System.out.println("    (" + obj1.getXOffset() + "," + obj0.getYOffset() + ")\n");


                Size size= ImageActions.getOAMSize(obj0.getShape(),obj1.getSize());


                int finalX = x;
                oams[x]= new NcerData.OAM()
                {
                    @Override
                    public NcerData.Obj0 getObj0()
                    {
                        return obj0;
                    }

                    @Override
                    public NcerData.Obj1 getObj1()
                    {
                        return obj1;
                    }

                    @Override
                    public NcerData.Obj2 getObj2()
                    {
                        return obj2;
                    }

                    @Override
                    public int getWidth()
                    {
                        return size.width;
                    }

                    @Override
                    public int getHeight()
                    {
                        return size.height;
                    }

                    @Override
                    public int getCellNumber()
                    {
                        return finalX;
                    }
                };
            }

            NcerData.OAM[] finalOams = sortOAM(oams);
            banks[i]= new NcerData.Bank()
            {
                @Override
                public int getNumCells()
                {
                    return cellNumber;
                }

                @Override
                public int getReadOnlyCellInfo()
                {
                    return readOnlyCellInfo;
                }

                @Override
                public long getCellOffset()
                {
                    return cellOffset;
                }

                @Override
                public long getPartitionOffset()
                {
                    return partitionOffset;
                }

                @Override
                public long getPartitionSize()
                {
                    return partitionSize;
                }

                @Override
                public NcerData.OAM[] getOAMs()
                {
                    return finalOams;
                }

                @Override
                public short getXMax()
                {
                    return xMax;
                }

                @Override
                public short getYMax()
                {
                    return yMax;
                }

                @Override
                public short getXMin()
                {
                    return xMin;
                }

                @Override
                public short getYMin()
                {
                    return yMin;
                }
            };

            buffer= new Buffer(file);
            buffer.skipTo(position);
        }

        //Label
        buffer.skipTo(headerSize + cellBankSectionSize);
        ArrayList<Long> offsetList= new ArrayList<>();
        ArrayList<String> nameList= new ArrayList<>();

        String labelID= buffer.readString(4);
        long labelSectionSize= 0;
        long[] offsets= null;
        if(labelID.equals("LBAL"))
        {
            labelSectionSize= buffer.readUInt32();

                //Name offset
            for(int i= 0; i < numBanks; i++)
            {
                long offset= buffer.readUInt32();
                if(offset >= labelSectionSize - 8)
                {
                    long currentPos= buffer.getPosition() & 0xffffffffL;
                    buffer= new Buffer(file);
                    buffer.skipTo(currentPos - 4);
                    break;
                }
                offsetList.add(offset);
            }
            offsets= offsetList.stream().mapToLong(Long::longValue).toArray();

                //Names
            for(int i= 0; i < offsets.length; i++)
            {
                StringBuilder str= new StringBuilder();
                byte c= (byte) (buffer.readByte() & 0xff);
                while (c != 0)
                {
                    str.append((char)c);
                    c= (byte) (buffer.readByte() & 0xff);
                }
                nameList.add(str.toString());
            }
        }

        String[] names= nameList.toArray(new String[0]);
//        names= Arrays.copyOf(names,numBanks);
//
//        for(int i= 0; i < names.length; i++)
//        {
//            if(names[i].equals(""))
//                names[i]= "" + i;
//        }


        //UEXT
        String uextID= buffer.readString(4);
        long uextSectionSize= 0;
        long uextUnknown= 0;
        if(uextID.equals("UEXT"))
        {
            uextSectionSize= buffer.readUInt32();
            uextUnknown= buffer.readUInt32();
        }

        try
        {
            buffer.close();
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }


        //Establishing final variables
        long finalMaxPartitionSize = maxPartitionSize;
        long finalFirstPartitionDataOffset = firstPartitionDataOffset;
        long finalLabelSectionSize = labelSectionSize;
        long[] finalOffsets = offsets;
        String[] finalNames = names;
        long finalUextSectionSize = uextSectionSize;
        long finalUextUnknown = uextUnknown;
        return new NcerData()
        {
            @Override
            public Header getHeader()
            {
                return new Header()
                {
                    @Override
                    public String getMagicId()
                    {
                        return headerID;
                    }

                    @Override
                    public int getEndianness()
                    {
                        return endianness;
                    }

                    @Override
                    public int getConstant()
                    {
                        return constant;
                    }

                    @Override
                    public long getFileSize()
                    {
                        return fileSize;
                    }

                    @Override
                    public int getHeaderSize()
                    {
                        return headerSize;
                    }

                    @Override
                    public int getNumSections()
                    {
                        return numSections;
                    }
                };
            }

            @Override
            public CellBank getCellBank()
            {
                return new CellBank()
                {
                    @Override
                    public String getMagicId()
                    {
                        return cellBankID;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return cellBankSectionSize;
                    }

                    @Override
                    public int getNumBanks()
                    {
                        return numBanks;
                    }

                    @Override
                    public int getBankTypes()
                    {
                        return bankType;
                    }

                    @Override
                    public long getBankDataOffset()
                    {
                        return bankDataOffset;
                    }

                    @Override
                    public long getBlockSize()
                    {
                        return blockSize;
                    }

                    @Override
                    public long getPartitionDataOffset()
                    {
                        return partitionDataOffset;
                    }

                    @Override
                    public long getUnused()
                    {
                        return unused;
                    }

                    @Override
                    public Bank[] getBanks()
                    {
                        return banks;
                    }

                    @Override
                    public long getMaxPartitionSize()
                    {
                        return finalMaxPartitionSize;
                    }

                    @Override
                    public long getFirstPartitionDataOffset()
                    {
                        return finalFirstPartitionDataOffset;
                    }
                };
            }

            @Override
            public Label getLabel()
            {
                return new Label()
                {
                    @Override
                    public String getMagicId()
                    {
                        return labelID;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return finalLabelSectionSize;
                    }

                    @Override
                    public long[] getOffsets()
                    {
                        return finalOffsets;
                    }

                    @Override
                    public String[] getNames()
                    {
                        return finalNames;
                    }
                };
            }

            @Override
            public UEXT getUEXT()
            {
                return new UEXT()
                {
                    @Override
                    public String getMagicId()
                    {
                        return uextID;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return finalUextSectionSize;
                    }

                    @Override
                    public long getUnknown()
                    {
                        return finalUextUnknown;
                    }
                };
            }
        };
    }
    
    private static NcerData.Obj0 getObj0(int value)
    {
        System.out.println("Obj0: " + Integer.toHexString(value));
        return new NcerData.Obj0()
        {
            @Override
            public int getYOffset()
            {
                return (byte)(value & 0xff);
            }

            @Override
            public boolean getRsFlag()
            {
                return ((value >> 8) & 1) == 0;
            }

            @Override
            public byte getObjDisable()
            {
                return (byte) (getRsFlag() ? (value >> 9) & 1 : 0);
            }

            @Override
            public byte getDoubleSize()
            {
                return (byte) (!getRsFlag() ? (value >> 9) & 1 : 0);
            }

            @Override
            public byte getObjMode()
            {
                return (byte)((value >> 10) & 3);
            }

            @Override
            public byte getMosaicFlag()
            {
                return (byte)((value >> 12) & 1);
            }

            @Override
            public byte getDepth()
            {
                return (byte)((value >> 13) & 1);
            }

            @Override
            public byte getShape()
            {
                return (byte)((value >> 14) & 3);
            }
        };
    }

    private static NcerData.Obj1 getObj1(int value, boolean flag)
    {
        System.out.println("Obj1: " + Integer.toHexString(value));
        return new NcerData.Obj1()
        {
            @Override
            public long getXOffset()
            {
                return (value & 0x01ff) >= 0x100 ? (value & 0x01ff) - 0x200 : (value & 0x01ff);
            }

            @Override
            public byte getUnused()
            {
                return flag ? (byte)((value >> 9) & 7) : 0;
            }

            @Override
            public byte getFlipX()
            {
                return flag ? (byte)((value >> 12) & 1) : 0;
            }

            @Override
            public byte getFlipY()
            {
                return flag ? (byte)((value >> 13) & 1) : 0;
            }

            @Override
            public byte getSelectParameter()
            {
                return !flag ? (byte)((value >> 9) & 0x1F) : 0;
            }

            @Override
            public byte getSize()
            {
                return (byte)((value >> 14) & 3);
            }
        };
    }

    private static NcerData.Obj2 getObj2(int value)
    {
        System.out.println("Obj2: " + Integer.toHexString(value));
        return new NcerData.Obj2()
        {
            @Override
            public long getTileOffset()
            {
                return value & 0x03FFL;
            }

            @Override
            public byte getPriority()
            {
                return (byte)((value >> 10) & 3);
            }

            @Override
            public byte getIndexPalette()
            {
                return (byte)((value >> 12) & 0xF);
            }
        };
    }

    private static NcerData.OAM[] sortOAM(NcerData.OAM[] arr)
    {
        int n = arr.length;

        for (int i = 0; i < n-1; i++)
        {
            int minIdx = i;
            for (int j = i+1; j < n; j++)
                if (arr[j].getObj2().getPriority() < arr[minIdx].getObj2().getPriority())
                    minIdx = j;

            NcerData.OAM temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }

        return arr;
    }

}
