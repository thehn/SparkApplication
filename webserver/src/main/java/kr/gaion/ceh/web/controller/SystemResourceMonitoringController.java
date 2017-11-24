package kr.gaion.ceh.web.controller;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.elasticsearch.monitor.os.OsInfo;
import org.elasticsearch.monitor.os.OsService;
import org.elasticsearch.monitor.os.OsStats;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.Swap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemResourceMonitoringController {
	
	@RequestMapping(value = "/system-resource-monitoring")
	public String showSystemResourceMonitoring(ModelMap model) throws SigarException {
		Sigar sigar = new Sigar();
		
		long[] pidArr = sigar.getProcList();
		String appName = "";
		int cpuCount = 0;
		int memCount = 0;
		for(long pid : pidArr) {
			try {
				//System.out.println("Process Name : " + sigar.getProcExe(pid).getName());				
				//System.out.println("Process Memory : " + BitToMb(sigar.getProcMem(pid).getSize()));
				//System.out.println("Process Cpu : " + sigar.getProcCpu(pid).getPercent());
				
				
				if(sigar.getProcCpu(pid).getTotal() > 0.0) {
					cpuCount++;
				}
				
				if(appName.isEmpty()) {
					appName = sigar.getProcExe(pid).getName();
				}
				else {
					if(!appName.equals(sigar.getProcExe(pid).getName())) {
						appName = sigar.getProcExe(pid).getName();
						memCount++;
					}
				}				
			} catch(SigarPermissionDeniedException ex) {
				//System.out.println("Permission Denied - " + pid);
			}			
		}
		
		CpuPerc cpu = sigar.getCpuPerc();
//		System.out.println("------------------------CPU Info");
//		System.out.println("User Time : " + CpuPerc.format(cpu.getUser()));
//		System.out.println("Sys Time : " + CpuPerc.format(cpu.getSys()));
//		System.out.println("Idle Time : " + CpuPerc.format(cpu.getIdle()));
		
		Mem mem = sigar.getMem();
		Swap swap = sigar.getSwap();
		
//        System.out.println("------------------------Memory Info");
//        System.out.println("Memory Total : " + ByteToGb(mem.getTotal()));
//        System.out.println("Memory Used : " + ByteToGb(mem.getUsed()));
//        System.out.println("Memory Free : " + ByteToGb(mem.getFree()));
//        
//        System.out.println("Swap Total : " + ByteToGb(swap.getTotal()));
//        System.out.println("Swap Used : " + ByteToGb(swap.getUsed()));
//        System.out.println("Swap Free : " + ByteToGb(swap.getFree()));
        
        FileSystem[] fileSystem = sigar.getFileSystemList();
        for(int index = 0; index < fileSystem.length; index++) {
        	FileSystemUsage fsUsage = sigar.getFileSystemUsage(sigar.getFileSystemList()[index].toString());
        	if(fsUsage != null) {
//        		System.out.println("-------------------------Disk Info [" + sigar.getFileSystemList()[index].toString() + "]");
//        		System.out.println("Disk Total : " + KbToGb(fsUsage.getTotal()));
//                System.out.println("Disk Usage : " + KbToGb(fsUsage.getUsed()));
//                System.out.println("Disk Avail : " + KbToGb(fsUsage.getAvail()));
        	}
        }
        
        // For Network
        //System.out.println("-----Network Info-----");
        //System.out.println(sigar.getNetInfo());
        NetStat netState = sigar.getNetStat();
//        System.out.println("in-bound " + netState.getAllInboundTotal()
//                + " out-bound " + netState.getAllOutboundTotal());
		
        long[] pidArrtrend = sigar.getProcList();
		String appNametrend = "";
		long firstPid = 0;
		long secondPid = 0;
		long thirdPid = 0;
		long fourthPid = 0;
		long fifthPid = 0;
		double sameApp = 0;
		for(long trendpid : pidArrtrend) {
			try {
				//System.out.println("State Name : " + sigar.getProcState(pid).getName());
				//System.out.println("ProcCpu % : " + sigar.getProcCpu(pid).getPercent());				
				//System.out.println("Process Memory : " + BitToMb(sigar.getProcMem(pid).getSize()));
				String user = sigar.getProcCredName(trendpid).getUser();
				
				if(firstPid == 0) {
					firstPid = trendpid;
					continue;
				} else if(secondPid == 0) {
					secondPid = trendpid;
					continue;
				} else if(thirdPid == 0) {
					thirdPid = trendpid;
					continue;
				} else if(fourthPid == 0) {
					fourthPid = trendpid;
					continue;
				} else if(fifthPid == 0) {
					fifthPid = trendpid;
					continue;
				}
				if(firstPid != 0 && secondPid != 0 && thirdPid != 0 && fourthPid != 0 && fifthPid != 0
						&& sigar.getProcTime(trendpid).getTotal() > 0.0) {
					if(sigar.getProcTime(trendpid).getTotal() > sigar.getProcTime(firstPid).getTotal()) {
						firstPid = trendpid;
					}
					else if(sigar.getProcTime(trendpid).getTotal() < sigar.getProcTime(firstPid).getTotal()
						&& sigar.getProcTime(trendpid).getTotal() > sigar.getProcTime(secondPid).getTotal()) {
						secondPid = trendpid;
					}
					else if(sigar.getProcTime(trendpid).getTotal() < sigar.getProcTime(secondPid).getTotal()
						&& sigar.getProcTime(trendpid).getTotal() > sigar.getProcTime(thirdPid).getTotal()) {
						thirdPid = trendpid;
					}
					else if(sigar.getProcTime(trendpid).getTotal() < sigar.getProcTime(thirdPid).getTotal()
							&& sigar.getProcTime(trendpid).getTotal() > sigar.getProcTime(fourthPid).getTotal()) {
						fourthPid = trendpid;
					}
					else if(sigar.getProcTime(trendpid).getTotal() < sigar.getProcTime(fourthPid).getTotal()
							&& sigar.getProcTime(trendpid).getTotal() > sigar.getProcTime(fifthPid).getTotal()) {
						fifthPid = trendpid;
					}
				}
				
				if(appNametrend.isEmpty()) {
					appNametrend = sigar.getProcState(trendpid).getName();
				}
				else {
					if(!appNametrend.equals(sigar.getProcState(trendpid).getName())) {
						appNametrend = sigar.getProcState(trendpid).getName();
					}
				}
			} catch(Exception ex) {
				//System.out.println("Permission Denied - " + pid);
				continue;
			}
		}
		
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		
		
		model.addAttribute("Time", sdf.format(dt).toString());
		model.addAttribute("FirstName",sigar.getProcState(firstPid).getName());
		model.addAttribute("FirstData", String.valueOf(sigar.getProcTime(firstPid).getTotal() /100));	
		model.addAttribute("SecondName",sigar.getProcState(secondPid).getName());
		model.addAttribute("SecondData", String.valueOf(sigar.getProcTime(secondPid).getTotal() /100));
		model.addAttribute("ThirdName",sigar.getProcState(thirdPid).getName());
		model.addAttribute("ThirdData", String.valueOf(sigar.getProcTime(thirdPid).getTotal() /100));
		model.addAttribute("FourthName",sigar.getProcState(fourthPid).getName());
		model.addAttribute("FourthData", String.valueOf(sigar.getProcTime(fourthPid).getTotal() /100));
		model.addAttribute("FifthName",sigar.getProcState(fifthPid).getName());
		model.addAttribute("FifthData", String.valueOf(sigar.getProcTime(fifthPid).getTotal() /100));
		
        model.addAttribute("CpuHosts", String.valueOf(cpuCount));
        model.addAttribute("CpuUser", CpuPerc.format(cpu.getUser()));
        model.addAttribute("CpuSys", CpuPerc.format(cpu.getSys()));
        model.addAttribute("CpuIdle", CpuPerc.format(cpu.getIdle()));
        model.addAttribute("CpuUtilization", CpuPerc.format(cpu.getUser() + cpu.getSys()));
        model.addAttribute("MemoryHosts", String.valueOf(memCount));
        model.addAttribute("MemoryTotal", ByteToGb(mem.getTotal()));
        model.addAttribute("MemoryUsed", ByteToGb(mem.getUsed()));
        model.addAttribute("MemoryFree", ByteToGb(mem.getFree()));
        model.addAttribute("SwapTotal", ByteToGb(swap.getTotal()));
        model.addAttribute("SwapUsed", ByteToGb(swap.getUsed()));
        model.addAttribute("SwapFree", ByteToGb(swap.getFree()));
        model.addAttribute("InBound", String.valueOf(netState.getAllInboundTotal()));
        model.addAttribute("OutBound", String.valueOf(netState.getAllOutboundTotal()));
        
        for(int index = 0; index < fileSystem.length; index++) {
        	FileSystemUsage fsUsage = sigar.getFileSystemUsage(sigar.getFileSystemList()[index].toString());
        	if(fsUsage != null) {
        		model.addAttribute("DiskTotal" + sigar.getFileSystemList()[index].toString().substring(0, 1), KbToGb(fsUsage.getTotal()));
        		model.addAttribute("DiskUsage" + sigar.getFileSystemList()[index].toString().substring(0, 1), KbToGb(fsUsage.getUsed()));
        		model.addAttribute("DiskAvailable" + sigar.getFileSystemList()[index].toString().substring(0, 1), KbToGb(fsUsage.getAvail()));
        	}
        }        
		return "system-resource-monitoring";
	}	
	
	@RequestMapping(value="/hosts")
	public @ResponseBody HashMap<String, Object> getHosts() throws Exception {
		Sigar sigar = new Sigar();
		
		long[] hostspidArr = sigar.getProcList();
		String appName = "";
		int cpuCount = 0;
		int memCount = 0;
		for(long hostspid : hostspidArr) {
			try {
				//System.out.println("Process Name : " + sigar.getProcExe(pid).getName());				
				//System.out.println("Process Memory : " + BitToMb(sigar.getProcMem(pid).getSize()));
				//System.out.println("Process Cpu : " + sigar.getProcCpu(pid).getPercent());
				
				//System.out.println(sigar.getProcCpu(hostspid).getTotal());
				if(sigar.getProcCpu(hostspid).getTotal() > 0.0) {
					cpuCount++;
				}
				
				if(appName.isEmpty()) {
					appName = sigar.getProcExe(hostspid).getName();
				}
				else {
					if(!appName.equals(sigar.getProcExe(hostspid).getName())) {
						appName = sigar.getProcExe(hostspid).getName();
						memCount++;
					}
				}				
			} catch(SigarPermissionDeniedException ex) {
				//System.out.println("Permission Denied - " + pid);
			}			
		}
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("CPU_HOSTS", cpuCount);
		hashMap.put("MEM_HOSTS", memCount);
	
		return hashMap;
	}
	
	@RequestMapping(value="/chartdata")
	public @ResponseBody HashMap<String, Object> getChartdata() throws Exception {
		Sigar sigar = new Sigar();
		
		CpuPerc cpu = sigar.getCpuPerc();
		Mem mem = sigar.getMem();
		Swap swap = sigar.getSwap();
		NetStat netState = sigar.getNetStat();
		
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		
		
		int index;
		FileSystem[] fileSystem = sigar.getFileSystemList();
		FileSystemUsage fsUsage = null;
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("TIME", sdf.format(dt).toString());
		hashMap.put("CPU_USE", CpuPerc.format(cpu.getUser() + cpu.getSys()));
		hashMap.put("RAM_USE", ByteToGb(mem.getUsed()));
		hashMap.put("RAM_TOTAL", ByteToGb(mem.getTotal()));
		hashMap.put("SWAP_USE",ByteToGb(swap.getUsed()));
		hashMap.put("SWAP_TOTAL", ByteToGb(swap.getTotal()));
		hashMap.put("IN_BOUND", String.valueOf(netState.getAllInboundTotal()));
		hashMap.put("OUT_BOUND", String.valueOf(netState.getAllOutboundTotal()));	
		
		for(index = 0; index < fileSystem.length; index++) {
	        fsUsage = sigar.getFileSystemUsage(sigar.getFileSystemList()[index].toString());
	        	if(fsUsage != null) {
	        		hashMap.put(sigar.getFileSystemList()[index].toString().substring(0, 1)+"_TOTAL", KbToGb(fsUsage.getTotal()));
	        		hashMap.put(sigar.getFileSystemList()[index].toString().substring(0, 1)+"_USE", KbToGb(fsUsage.getUsed()));
	        		hashMap.put(sigar.getFileSystemList()[index].toString().substring(0, 1)+"_AVAIL", KbToGb(fsUsage.getAvail()));
	        	}   
		 
		 	}      
		
		return hashMap;
	}

	
	
	@RequestMapping(value="/cputrend")
	public @ResponseBody HashMap<String, Object> getCpuTrend() throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Sigar sigar = new Sigar();
		
		long[] cputrendpidArr = sigar.getProcList();
		String appName = "";
		long firstPid = 0;
		long secondPid = 0;
		long thirdPid = 0;
		long fourthPid = 0;
		long fifthPid = 0;
		double sameApp = 0;
		for(long cputrendpid : cputrendpidArr) {
			try {
				//System.out.println("State Name : " + sigar.getProcState(pid).getName());
				//System.out.println("ProcCpu % : " + sigar.getProcCpu(pid).getPercent());				
				//System.out.println("Process Memory : " + BitToMb(sigar.getProcMem(pid).getSize()));
				String user = sigar.getProcCredName(cputrendpid).getUser();
				
				if(firstPid == 0) {
					firstPid = cputrendpid;
					continue;
				} else if(secondPid == 0) {
					secondPid = cputrendpid;
					continue;
				} else if(thirdPid == 0) {
					thirdPid = cputrendpid;
					continue;
				} else if(fourthPid == 0) {
					fourthPid = cputrendpid;
					continue;
				} else if(fifthPid == 0) {
					fifthPid = cputrendpid;
					continue;
				}
				if(firstPid != 0 && secondPid != 0 && thirdPid != 0 && fourthPid != 0 && fifthPid != 0
						&& sigar.getProcTime(cputrendpid).getTotal() > 0.0) {
					if(sigar.getProcTime(cputrendpid).getTotal() > sigar.getProcTime(firstPid).getTotal()) {
						firstPid = cputrendpid;
					}
					else if(sigar.getProcTime(cputrendpid).getTotal() < sigar.getProcTime(firstPid).getTotal()
						&& sigar.getProcTime(cputrendpid).getTotal() > sigar.getProcTime(secondPid).getTotal()) {
						secondPid = cputrendpid;
					}
					else if(sigar.getProcTime(cputrendpid).getTotal() < sigar.getProcTime(secondPid).getTotal()
						&& sigar.getProcTime(cputrendpid).getTotal() > sigar.getProcTime(thirdPid).getTotal()) {
						thirdPid = cputrendpid;
					}
					else if(sigar.getProcTime(cputrendpid).getTotal() < sigar.getProcTime(thirdPid).getTotal()
							&& sigar.getProcTime(cputrendpid).getTotal() > sigar.getProcTime(fourthPid).getTotal()) {
						fourthPid = cputrendpid;
					}
					else if(sigar.getProcTime(cputrendpid).getTotal() < sigar.getProcTime(fourthPid).getTotal()
							&& sigar.getProcTime(cputrendpid).getTotal() > sigar.getProcTime(fifthPid).getTotal()) {
						fifthPid = cputrendpid;
					}
				}
				
				if(appName.isEmpty()) {
					appName = sigar.getProcState(cputrendpid).getName();
				}
				else {
					if(!appName.equals(sigar.getProcState(cputrendpid).getName())) {
						appName = sigar.getProcState(cputrendpid).getName();
					}
				}
			} catch(Exception ex) {
				//System.out.println("Permission Denied - " + pid);
				continue;
			}
		}
		
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		
		
		hashMap.put("TIME", sdf.format(dt).toString());
		hashMap.put("First_Name",sigar.getProcState(firstPid).getName());
		hashMap.put("First_Data", String.valueOf(sigar.getProcTime(firstPid).getTotal() /100));	
		hashMap.put("Second_Name",sigar.getProcState(secondPid).getName());
		hashMap.put("Second_Data", String.valueOf(sigar.getProcTime(secondPid).getTotal() /100));
		hashMap.put("Third_Name",sigar.getProcState(thirdPid).getName());
		hashMap.put("Third_Data", String.valueOf(sigar.getProcTime(thirdPid).getTotal() /100));
		hashMap.put("Fourth_Name",sigar.getProcState(fourthPid).getName());
		hashMap.put("Fourth_Data", String.valueOf(sigar.getProcTime(fourthPid).getTotal() /100));
		hashMap.put("Fifth_Name",sigar.getProcState(fifthPid).getName());
		hashMap.put("Fifth_Data", String.valueOf(sigar.getProcTime(fifthPid).getTotal() /100));
		
//		hashMap.put('"' + "time" + '"', sdf.format(dt).toString());
//		hashMap.put('"' + sigar.getProcState(firstPid).getName()+'"', String.valueOf(sigar.getProcTime(firstPid).getTotal() /100));		
//		hashMap.put('"' + sigar.getProcState(secondPid).getName()+'"', String.valueOf(sigar.getProcTime(secondPid).getTotal() /100));
//		hashMap.put('"' + sigar.getProcState(thirdPid).getName()+'"', String.valueOf(sigar.getProcTime(thirdPid).getTotal() /100));
//		hashMap.put('"' + sigar.getProcState(fourthPid).getName()+'"', String.valueOf(sigar.getProcTime(fourthPid).getTotal() /100));
//		hashMap.put('"' + sigar.getProcState(fifthPid).getName()+'"', String.valueOf(sigar.getProcTime(fifthPid).getTotal() /100));
		
		/*System.out.println(sigar.getProcState(firstPid).getName() + " : " + sigar.getProcTime(firstPid).getTotal() /100);
		System.out.println(sigar.getProcState(secondPid).getName() + " : " + sigar.getProcTime(secondPid).getTotal() /100);
		System.out.println(sigar.getProcState(thirdPid).getName() + " : " + sigar.getProcTime(thirdPid).getTotal() /100);
		System.out.println(sigar.getProcState(fourthPid).getName() + " : " + sigar.getProcTime(fourthPid).getTotal() /100);
		System.out.println(sigar.getProcState(fifthPid).getName() + " : " + sigar.getProcTime(fifthPid).getTotal() /100);
		*/
		return hashMap;
	}

	public static String BitToMb(double value) {
        return Double.toString(Math.round(new Double(value / 8 / 1024 / 1024) * 100d) / 100d);
   }
	
	public static String ByteToGb(double value) {
         return Double.toString(Math.round(new Double(value / 1024 / 1024 / 1024) * 100d) / 100d);
    }
	
	public static String KbToGb(double value) {
        return Double.toString(Math.round(new Double(value / 1024 / 1024) * 100d) / 100d);
   }
}
