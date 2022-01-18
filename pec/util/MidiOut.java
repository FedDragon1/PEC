package pec.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
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

/**
 * Converts Midi files to Excel work sheets / {@code .mcfunction} file
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class MidiOut {
	private String OUT_PATH = ".";
	private String MIDI_PATH;
	private int HEIGHT = 200;
	private int BASE = 4;
    private static final int NOTE = 0x90;
    
    public MidiOut(String OUT_PATH, String MIDI_PATH, int BASE) {
    	this.OUT_PATH = OUT_PATH;
    	this.MIDI_PATH = MIDI_PATH;
    	this.BASE = BASE;
    }
    
    public MidiOut(String OUT_PATH, String MIDI_PATH) {
    	this.OUT_PATH = OUT_PATH;
    	this.MIDI_PATH = MIDI_PATH;
    }
    
    public MidiOut(String MIDI_PATH, int BASE) {
    	this.MIDI_PATH = MIDI_PATH;
    	this.BASE = BASE;
    }
    
    public MidiOut(String MIDI_PATH) {
    	this.MIDI_PATH = MIDI_PATH;
    }
    
    public void changeHeight(int height) {
    	HEIGHT = height;
    }
    
    /**
     * Converts {@code NOTE_ON} event to blocks
     * 
     * @param noteLength Length of a 16th note
     * @param PATH Midi file path
     * @return All blocks' coordinate
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    private static int[][][][] getAllBlocks(int noteLength, String PATH) throws InvalidMidiDataException, IOException {
    	Sequence sequence = MidiSystem.getSequence(new File(PATH));
    	int[][][][] blocks = new int[sequence.getTracks().length][1][1][2];	//[track][group][same-x key][key]
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
        	boolean flag = false;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //long oldTick = 0l;
            long oldX = 0l;
            int groupCount = 0;
            int noteCount = 0;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long Tick = event.getTick();
                        long x = (long) (Tick / 120d * noteLength + noteLength * 16);
                        int y = sm.getData1() - 60;
                        int[] key = {(int)x, y};
                    	if(x == oldX) {
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
                    	//oldTick = Tick;
                    	oldX = x;
                		flag = true;
                		
                    }
                }
            }
            trackNumber++;
            System.out.println();
        }
        return blocks;
    }
    
    /**
     * Directly convert Midi file to blocks in Minecraft
     * 
     * @param blocks Name of blocks
     * @throws Exception
     */
    public void blocks(String... blocks) throws Exception {
    	File notes = new File(OUT_PATH + "\\note.mcfunction");
    	PrintStream ps;
    	try {
    		ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(notes)), true);
    	} catch (FileNotFoundException e) {
    		new File(OUT_PATH).mkdirs();
    		ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(notes)), true);
    	}
    	
    	Sequence sequence = MidiSystem.getSequence(new File(MIDI_PATH));
    	
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long Tick = event.getTick();
                    	//120 = 16分音符
                        long x = (long) (Tick / 120d * BASE + BASE * 16);
                        int key = sm.getData1();
                        int y = key - 60;
                        String command = String.format("setblock %d %d %d minecraft:%s", x, HEIGHT, y, blocks[trackNumber-1]);
                        ps.println(command);
                    }
                }
            }

            System.out.println();
        }
        ps.close();
    }
    
    /**
     * Get the index of line
     * 
     * @param raw Input value
     * @return {@code raw - 1} if integer, else {@code (int)raw}
     */
    private static int round1(double raw) {
    	if(raw == (int)raw) {
    		raw--;
    	} else {
    		raw = Math.floor(raw);
    	}
    	return (int)raw;
    }
    
    /**
     * Generate an Excel Workbook based on Midi file to modify the string property
     * 
     * @throws Exception
     */
    public void lines() throws Exception {
        File excel = new File(OUT_PATH + "\\line.xlsx");
        PrintStream ps;
        try {
        	ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(excel)), true);
        } catch (FileNotFoundException e) {
        	new File(OUT_PATH).mkdirs();
        	ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(excel)), true);
        }
        Sequence sequence = MidiSystem.getSequence(new File(MIDI_PATH));
        XSSFWorkbook workbook = new XSSFWorkbook();
        int trackNumber = 0;
        XSSFSheet[] sheet = new XSSFSheet[sequence.getTracks().length];

        for (int i = 0; i < sequence.getTracks().length; i++) {
        	sheet[i] = workbook.createSheet("Line - Track" + (i+1));
        }
    	
        int[][][][] blocks = getAllBlocks(BASE, MIDI_PATH);
        //System.out.println(Arrays.deepToString(blocks));
        
        for(int[][][] track : blocks) {
        	XSSFSheet thisSheet = sheet[trackNumber];
        	//System.out.println(thisSheet);
            XSSFRow row;
            Map<String, Object[]> sheetData = new TreeMap<String, Object[]>();
            Object[] obj = new Object[8];
            obj[0] = "PEN";
            obj[1] = ".draw(\"";
            obj[2] = "KEY";
            obj[7] = "\");";
            int[][] oldkeyg = {{0,0}};
            int keysave = 0;
            for(int[][] group: track) {
            	//System.out.println(Arrays.deepToString(oldkeyg) + " -> " +Arrays.deepToString(group));
            	double ratio = (double)oldkeyg.length / (double)group.length;
            	int keycount;
            	if (ratio <= 1) {
            		for(keycount = 1; keycount <= group.length; keycount++) { //less to more
            			int[] nowKey = group[keycount - 1];
            			int[] oldKey = oldkeyg[round1(ratio * keycount)];
            			//System.out.println(Arrays.toString(nowKey));
            			obj[3] = "\", ";
            			obj[4] = Integer.toString(oldKey[0]);
            			obj[5] = String.format(", %d, %d, %d, %d, %d, \"", HEIGHT, oldKey[1], nowKey[0], HEIGHT, nowKey[1]);
            			obj[6] = "PARTICLE";
            			sheetData.put(Integer.toString(keysave + keycount), obj.clone());
            		}
            		keysave += keycount;
            	} else {
            		ratio = (double)group.length / (double)oldkeyg.length;
            		for(keycount = 1; keycount <= oldkeyg.length; keycount++) {
            			int[] nowKey = group[round1(ratio * keycount)];
            			int[] oldKey = oldkeyg[keycount - 1];
            			//System.out.println(Arrays.toString(nowKey));
            			obj[3] = "\", ";
            			obj[4] = Integer.toString(oldKey[0]);
            			obj[5] = String.format(", %d, %d, %d, %d, %d, \"", HEIGHT, oldKey[1], nowKey[0], HEIGHT, nowKey[1]);
            			obj[6] = "PARTICLE";
            			sheetData.put(Integer.toString(keysave + keycount), obj.clone());
            		}
            		keysave += keycount;
            	}
            	System.out.println(Arrays.deepToString(group));
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
    
    /**
     * Generate an Excel Workbook for modify disappear effects
     * 
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void disappear() throws InvalidMidiDataException, IOException {
        File excel = new File(OUT_PATH + "\\disappear.xlsx");
        PrintStream ps;
        try {
        	ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(excel)), true);
        } catch (FileNotFoundException e) {
        	new File(OUT_PATH).mkdirs();
        	ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(excel)), true);
        }
        Sequence sequence = MidiSystem.getSequence(new File(MIDI_PATH));
        XSSFWorkbook workbook = new XSSFWorkbook();
        int trackNumber = 0;
        XSSFSheet[] sheet = new XSSFSheet[sequence.getTracks().length];

        for (int i = 0; i < sequence.getTracks().length; i++) {
        	sheet[i] = workbook.createSheet("Disappear - Track" + (i+1));
        }
        
    	for (Track track : sequence.getTracks()) {
    		XSSFSheet thatSheet = sheet[trackNumber];
            XSSFRow row2;
            Map<String, Object[]> sheetData2 = new TreeMap<String, Object[]>();
            Object[] obj2 = new Object[8];
            obj2[0] = "PEN";
            obj2[7] = "\");";
            int rowCount2 = 1;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE && sm.getData2() != 0) {
                    	long Tick = event.getTick();
                        long x = (long) (Tick / 120d * BASE + BASE * 16);
                        int y = sm.getData1() - 60;
                        obj2[1] = String.format(".draw(%d, %d, %d, \"", x, HEIGHT, y);
                        obj2[2] = "EFFECT";
                        obj2[3] = "\", ";
                        obj2[4] = "PER SIDE PARTICLE";
                        obj2[5] = ", \"";
                        obj2[6] = "PARTICLE";
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
}