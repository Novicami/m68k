package m68k.cpu.instructions;

/**
 * ${FILE}
 * <p>
 * Federico Berti
 * <p>
 * Copyright 2018
 *
 */

import junit.framework.TestCase;
import m68k.cpu.Cpu;
import m68k.cpu.MC68000;
import m68k.memory.AddressSpace;
import m68k.memory.MemorySpace;

public class ADDTest2 extends TestCase {
    AddressSpace bus;
    Cpu cpu;


    public void setUp() {
        bus = new MemorySpace(1);    //create 1kb of memory for the cpu

        cpu = new MC68000();
        cpu.setAddressSpace(bus);
        cpu.reset();
        cpu.setAddrRegisterLong(7, 0x200);
    }

    public void testADD_byte_zeroFlag() {
        bus.writeWord(4, 0xd402);    // add.b d2,d2
        //this should be true as 0xFFFFFF00_byte -> 00
        testADD_byte_zeroFlag(cpu, true, 0xFFFF_FF80, 0xFFFF_FF00);
    }

    public void testADDQ_byte_zeroFlag() {
        bus.writeWord(4, 0x5402);    // addq.b #2,d2 or addi.b #2, d2
        testADD_byte_zeroFlag(cpu, true, 0xFFFF_FFFE, 0xFFFF_FF00);
    }

    public void testADD_word_zeroFlag() {
        bus.writeWord(4, 0xd442);    // add.w d2,d2
        //this should be true as 0xFFFFF0000_word -> 0000
        testADD_word_zeroFlag(cpu, true, 0xFFFF_8000, 0xFFFF_0000);
    }

    public void testADDQ_word_zeroFlag() {
        bus.writeWord(4, 0x5442);    // addq.w #2,d2 or addi.w #2, d2
        testADD_byte_zeroFlag(cpu, true, 0x0001_FFFE, 0x0001_0000);
    }

    private void testADD_byte_zeroFlag(Cpu cpu, boolean expectedZFlag, long d2_pre, long d2_post) {
        cpu.setPC(4);
        cpu.setDataRegisterLong(2, (int) d2_pre);
        cpu.execute();

        assertEquals(d2_post, cpu.getDataRegisterLong(2));
        assertEquals(0x00, cpu.getDataRegisterByte(2));
        assertEquals(expectedZFlag, cpu.isFlagSet(Cpu.Z_FLAG));
    }

    private void testADD_word_zeroFlag(Cpu cpu, boolean expectedZFlag, long d2_pre, long d2_post) {
        cpu.setPC(4);
        cpu.setDataRegisterLong(2, (int) d2_pre);
        cpu.execute();

        assertEquals(d2_post, cpu.getDataRegisterLong(2));
        assertEquals(0x0000, cpu.getDataRegisterWord(2));
        assertEquals(expectedZFlag, cpu.isFlagSet(Cpu.Z_FLAG));
    }
}
