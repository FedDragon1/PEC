package pec.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;


public class MidiOut {
	static Scanner sc = new Scanner(System.in);
	static int LENGTH = 0;
	static int HEIGHT = 0;
    public static final int NOTE = 0x90;

    private static int choose_block(float bpm) {
    	if (bpm <= 87) {
    		return 4;
    	} else if (bpm <= 112) {
    		return 3;
    	} else {
    		System.out.println("曲子的bpm过高，请自行选择时基对应的长度");
    		System.out.print("可以选择与bpm不匹配的格数并快进");
    		int num = sc.nextInt();
    		return num;
    	}
    }
    private static int[][][][] getAllBlocks(int noteLength, String PATH) throws InvalidMidiDataException, IOException {
    	Sequence sequence = MidiSystem.getSequence(new File(PATH));
    	int[][][][] blocks = new int[sequence.getTracks().length][1][1][2];	//[track][group][same-x key][key]
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
        	boolean flag = false;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            long oldTick = 0l;
            int groupCount = 0;
            int noteCount = 0;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long currentTick = event.getTick();
                    	long Tick = event.getTick();
                        long x = Tick / 120 * noteLength + noteLength * 16;
                        int y = sm.getData1() - 60;
                        int[] key = {(int)x, y};
                    	if(currentTick == oldTick) {
                    		if(flag) {
                    			blocks[trackNumber][groupCount] = Arrays.copyOf(blocks[trackNumber][groupCount], blocks[trackNumber][groupCount].length + 1);
                    			blocks[trackNumber][groupCount][blocks[trackNumber][groupCount].length - 1] = key;
                    			//System.out.println(Arrays.deepToString(blocks[trackNumber]) + (blocks[trackNumber][groupCount].length - 1));
                    		} else {
                    			blocks[trackNumber][groupCount][noteCount] = key;
                    		}
                    		noteCount++;
                    	} else {
                    		if(flag) {
                    			blocks[trackNumber] = Arrays.copyOf(blocks[trackNumber], blocks[trackNumber].length + 1);
                    			groupCount++;
                    			blocks[trackNumber][groupCount] = new int[1][2];
                    		}
                    		noteCount = 0;
                    		blocks[trackNumber][groupCount][noteCount] = key;
                    		//System.out.println(Arrays.deepToString(blocks[trackNumber]));
                    	}
                    	oldTick = currentTick;
                		flag = true;
                		
                    }
                }
            }
            trackNumber++;
            System.out.println();
        }
        return blocks;
    }
    private static void blocks() throws Exception {
    	File notes = new File("note.mcfunction");
    	System.out.print("midi文件路径：");
    	String PATH = sc.next();
    	PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(notes)), true);
    	Sequence sequence = MidiSystem.getSequence(new File(PATH));
        System.out.print("曲子的bpm？");
        float bpm = sc.nextFloat();
        int block = choose_block(bpm);
        System.out.println("根据曲子的bpm，推荐以 " + block + " 格为选择的时常音符生成");
        System.out.print("输入e进入选择界面，其他继续");
        String a = sc.next();
        if (a.equals("e")) {
        	System.out.print("时基对应方格数：");
        	block = sc.nextInt();
        }
        System.out.print("方块高度：");
        int height = sc.nextInt();
        String[] blocks = new String[sequence.getTracks().length];
        for (int n = 0; n <= sequence.getTracks().length - 1; n++) {
        	System.out.print("第" + (n+1) + "轨的方块：");
        	blocks[n] = sc.next();
        }
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long Tick = event.getTick();
                    	//120 = 16分音符
                        long x = Tick / 120 * block + block * 16;
                        int key = sm.getData1();
                        int y = key - 60;
                        String command = String.format("setblock %d %d %d minecraft:%s", x, height, y, blocks[trackNumber-1]);
                        ps.println(command);
                        System.out.println(command);
                    }
                }
            }

            System.out.println();
        }
        ps.close();
    }
    
    private static int round1(double raw) {
    	if(raw == (int)raw) {
    		raw--;
    	} else {
    		raw = Math.floor(raw);
    	}
    	return (int)raw;
    }
    
    private static void lines() throws Exception {
    	System.out.print("midi文件路径：");
    	String PATH = sc.next();
    	System.out.print("十六分音符格数：");
    	LENGTH = sc.nextInt();
    	System.out.print("方块高度：");
        HEIGHT = sc.nextInt();
        PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("line.xlsx"))), true);
        Sequence sequence = MidiSystem.getSequence(new File(PATH));
        XSSFWorkbook workbook = new XSSFWorkbook();
        int trackNumber = 0;
        XSSFSheet[] sheet = new XSSFSheet[sequence.getTracks().length];

        for (int i = 0; i < sequence.getTracks().length; i++) {
        	sheet[i] = workbook.createSheet("Line - Track" + (i+1));
        }
    	
        int[][][][] blocks = getAllBlocks(LENGTH, PATH);
        System.out.println(Arrays.deepToString(blocks));
        
        for(int[][][] track : blocks) {
        	XSSFSheet thisSheet = sheet[trackNumber];
        	System.out.println(thisSheet);
            XSSFRow row;
            Map<String, Object[]> sheetData = new TreeMap<String, Object[]>();
            Object[] obj = new Object[11];
            obj[0] = "Draw.";
            obj[1] = "MOVABLE/NORMAL";
            obj[2] = "(\"";
            obj[10] = ");";
            int[][] oldkeyg = {{0,0}};
            int keysave = 0;
            for(int[][] group: track) {
            	System.out.println(Arrays.deepToString(oldkeyg) + " -> " +Arrays.deepToString(group));
            	double ratio = (double)oldkeyg.length / (double)group.length;
            	int keycount;
            	if (ratio <= 1) {
            		for(keycount = 1; keycount <= group.length; keycount++) { //less to more
            			int[] nowKey = group[keycount - 1];
            			int[] oldKey = oldkeyg[round1(ratio * keycount)];
            			//System.out.println(Arrays.toString(nowKey));
            			obj[3] = "TYPE";
            			obj[4] = "\", ";
            			obj[5] = Integer.toString(oldKey[0]);
            			obj[6] = String.format(", %d, %d, %d, %d, \"", 
            					nowKey[0], 
            					oldKey[1], nowKey[1], HEIGHT);
            			obj[7] = "PARTICLE";
            			obj[8] = "\", ";
            			obj[9] = "SPIRAL DATA";
            			sheetData.put(Integer.toString(keysave + keycount), obj.clone());
            		}
            		keysave += keycount;
            	} else {
            		ratio = (double)group.length / (double)oldkeyg.length;
            		for(keycount = 1; keycount <= oldkeyg.length; keycount++) {
            			int[] nowKey = group[round1(ratio * keycount)];
            			int[] oldKey = oldkeyg[keycount - 1];
            			//System.out.println(Arrays.toString(nowKey));
            			obj[3] = "TYPE";
            			obj[4] = "\", ";
            			obj[5] = Integer.toString(oldKey[0]);
            			obj[6] = String.format(", %d, %d, %d, %d, \"", 
            					nowKey[0], 
            					oldKey[1], nowKey[1], HEIGHT);
            			obj[7] = "PARTICLE";
            			obj[8] = "\", ";
            			obj[9] = "SPIRAL DATA";
            			sheetData.put(Integer.toString(keysave + keycount), obj.clone());
            		}
            		keysave += keycount;
            	}
            	
            	oldkeyg = group;
            }
            //System.out.println(sheetData);
            Set<String> keyid = sheetData.keySet();
            int keyCount = 0;
            for (String key: keyid) {
            	row = thisSheet.createRow(keyCount++);
            	Object[] objArr = sheetData.get(key);
            	int cellCount = 0;
            	
            	for(Object objCell: objArr) {
            		Cell cell = row.createCell(cellCount++);
            		cell.setCellValue((String)objCell);
            	}
            }
            trackNumber++;
        }
        workbook.write(ps);
        ps.close();
        workbook.close();
    }
    private static void disappear() throws InvalidMidiDataException, IOException {
    	System.out.print("midi文件路径：");
    	String PATH = sc.next();
    	System.out.print("十六分音符格数：");
    	LENGTH = sc.nextInt();
    	System.out.print("方块高度：");
        HEIGHT = sc.nextInt();
        PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("disappear.xlsx"))), true);
        Sequence sequence = MidiSystem.getSequence(new File(PATH));
        XSSFWorkbook workbook = new XSSFWorkbook();
        int trackNumber = 0;
        XSSFSheet[] sheet = new XSSFSheet[sequence.getTracks().length];

        for (int i = 0; i < sequence.getTracks().length; i++) {
        	sheet[i] = workbook.createSheet("Disappear - Track" + (i+1));
        }
        
    	for (Track track :  sequence.getTracks()) {
    		XSSFSheet thatSheet = sheet[trackNumber];
        	System.out.println(thatSheet);
            XSSFRow row2;
            Map<String, Object[]> sheetData2 = new TreeMap<String, Object[]>();
            Object[] obj2 = new Object[7];
            obj2[0] = "Disappear.";
            obj2[1] = "FADE/RIPPLE";
            
            obj2[6] = "\");";
            int rowCount2 = 1;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long Tick = event.getTick();
                        long x = Tick / 120 * LENGTH + LENGTH * 16;
                        int y = sm.getData1() - 60;
                        obj2[2] = String.format("(%d, %d, %d, \"", x, y, HEIGHT);
                        obj2[3] = "EFFECT";
                        obj2[4] = "\", \"";
                        obj2[5] = "PARTICLE (ONLY RIPPLE)";
                        sheetData2.put(Integer.toString(rowCount2++), obj2.clone());
                    }
                }
            }
            Set<String> keyid2 = sheetData2.keySet();
            for (String key: keyid2) {
            	row2 = thatSheet.createRow(Integer.parseInt(key)-1);
            	Object[] objArr = sheetData2.get(key);
            	int cellCount = 0;
            	
            	for(Object objCell: objArr) {
            		Cell cell = row2.createCell(cellCount++);
            		cell.setCellValue((String)objCell);
            	}
            }
            
            trackNumber++;
    	}
    	workbook.write(ps);
        ps.close();
        workbook.close();
    }
    public static void main(String[] args) throws Exception {
    	System.out.println("输入1，转换midi文件至地图内方块");
    	System.out.println("输入2，转换midi文件至线条和消失特效（Excel）");
    	System.out.println("输入3，转换midi文件至消失特效（Excel）");
    	System.out.print("选择1，2，或3：");
    	int service = sc.nextInt();
    	switch (service) {
    	case 1:
    		blocks();
    		break;
    	case 2:
    		lines();
    		break;
    	case 3:
    		disappear();
    		break;
    	default:
    		throw new Exception("Invalid Input");
    	}
    }
}