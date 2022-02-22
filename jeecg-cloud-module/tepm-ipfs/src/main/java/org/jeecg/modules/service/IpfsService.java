package org.jeecg.modules.service;

import com.alibaba.fastjson.JSON;
import io.ipfs.api.JSONParser;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.entity.PinKeys;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zlf
 */
@Service
@Slf4j
public class IpfsService {

	/**
	 * ipfs的服务器地址和端口
	 */
	 //static IPFSS ipfs = new IPFSS("/ip4/172.1.92.117/tcp/5010");
	 //static IPFSS ipfs = new IPFSS("/ip4/192.168.10.101/tcp/5010");
//	 static IPFSS ipfs = new IPFSS("/ip4/192.168.1.243/tcp/5001");
	 static IPFSS ipfs = new IPFSS("/ip4/192.168.1.247/tcp/5010");
	 //static IPFSS ipfs = new IPFSS("/ip4/127.0.0.1/tcp/5001");

	public void ls(String fileHash) throws IOException {
		List<MerkleNode> merkleNodes = ipfs.ls(Multihash.fromBase58(String.valueOf(fileHash)));
		System.out.println(JSON.toJSONString(merkleNodes));
	}

	public Object filesLs(String arg) throws IOException {
		return  ipfs.filesLs(arg);
	}



	public void writeFile(NamedStreamable file, String arg ,long filePathId) throws IOException {
		ipfs.writeFile(file,arg ,filePathId);
	}
	public boolean addFileSection(java.io.File file, String arg,long filePathId) {
		return ipfs.addFileSection(file,arg ,filePathId);
	}
	public boolean fileCp(String source ,String destination ) {
		return ipfs.fileCp( source , destination );
	}
	public String filesStatHash(String arg) {
		return ipfs.filesStatHash(arg);
	}


	public boolean filesStat(String arg) {
		return ipfs.filesStat(arg);
	}
	public boolean filesMkdir(String arg) {
		return ipfs.filesMkdir(arg);
	}


	/**
	 * 上传文件夹/文件
	 * @param filePathName 文件路径/文件名
	 * @return
	 * @throws IOException
	 */
	public String upload(String filePathName) throws IOException {
		//判断操作系统是否是Windows
		if (isWindowsOS()) {
			filePathName = "c:"+filePathName;
		}else {
			if (!filePathName.contains("/home/")&&!filePathName.contains("/home/")){
				filePathName = "/home"+filePathName;
			}
		}
		File upFile = new File(filePathName);
		//filePathName指的是文件的上传路径+文件名，如D:/1.png
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(upFile);
		List<MerkleNode> nodes = ipfs.add(file);
		AtomicReference<String> resultHash = new AtomicReference<>("");
		nodes.forEach(n->{
			log.info("file:"+JSON.toJSONString(n));
			if (upFile.getName().equals(n.name.get())){
				resultHash.set(n.hash.toString());
			}
		});
		return resultHash.get();
	}

	public String uploadFile(File upFile) throws IOException {
		//filePathName指的是文件的上传路径+文件名，如D:/1.png
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(upFile);
		List<MerkleNode> nodes = ipfs.add(file);
		AtomicReference<String> resultHash = new AtomicReference<>("");
		nodes.forEach(n->{
			log.info("file:"+JSON.toJSONString(n));
			if (upFile.getName().equals(n.name.get())){
				resultHash.set(n.hash.toString());
			}
		});
		return resultHash.get();
	}

//	public ReturnUpload uscriptUloadFile(File upFile,String fileName) throws IOException {
//		//filePathName指的是文件的上传路径+文件名，如D:/1.png
//		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(upFile);
//		List<MerkleNode> nodes = ipfs.add(file);
//		AtomicReference<String> resultHash = new AtomicReference<>("");
//		ReturnUpload returnUpload = new ReturnUpload();
//
//		returnUpload.setFileName(fileName);
//		nodes.forEach(n->{
//			log.info("file:"+JSON.toJSONString(n));
//			if (upFile.getName().equals(n.name.get())){
//				resultHash.set(n.hash.toString());
//			}
//		});
//		returnUpload.setHash(resultHash.get());
//		return returnUpload;
//	}


	/**
	 * 下载文件夹/文件
	 * @param filePathName 文件路径/文件名
	 * @param hash hash值
	 * @throws IOException
	 */
	public String download(String filePathName,String hash) throws IOException {
		//判断操作系统是否是Windows
		if (isWindowsOS()) {
			if (!filePathName.contains("C:\\")&&!filePathName.contains("C:/")){
				filePathName = "C:"+filePathName;
			}
		}else {
			if (!filePathName.contains("\\home\\")&&!filePathName.contains("/home/")){
				filePathName = "/home"+filePathName;
			}
		}
		String finalFilePathName = filePathName;
		Multihash filePointer = Multihash.fromBase58(hash);
		//判断是文件还是文件夹
		File fileNode  = new File(filePathName);
		if (!fileNode.getName().contains(".")){
			//文件夹
			List<MerkleNode> nodes = ipfs.ls(filePointer);
			nodes.forEach(n->{
				try {
					Multihash filePointer1 = Multihash.fromBase58(String.valueOf(n.hash));
					log.info("fine Name:"+n.name.get()+",hash:"+n.hash);
					createFile(fileNode);
					File file  = new File(finalFilePathName +"/"+n.name.get());
					if(!fileNode.getName().contains(".")) {
						createFile(file);
						//download(finalFilePathName +"/"+n.name.get()+"\\",String.valueOf(n.hash));
						download(finalFilePathName +"/"+n.name.get(),String.valueOf(n.hash));
					}else {
						byte[] data = ipfs.cat(filePointer1);
						if(data != null){
							if(file.exists()){
								file.delete();
							}
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(data,0,data.length);
							fos.flush();
							fos.close();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}else {
			//文件
			byte[] data = ipfs.cat(filePointer);
			if(data != null){
				filePathName = filePathName.replaceAll("\\\\","/");
				File file  = new File(filePathName);
				if(file.exists()){
					file.delete();
				}
				//获得全路径
				File fg = file.getParentFile();
				String path = fg.getPath()+"\\";
				if (!isWindowsOS()){
					path = path.replaceAll("\\\\","/");
				}
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				log.info("查看日志filePath:" + filePath);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data,0,data.length);
				fos.flush();
				fos.close();
			}
		}
		return finalFilePathName;
	}

	public byte[] downloadFile(String hash) throws IOException {
		Multihash filePointer = Multihash.fromBase58(hash);
		byte[] data = ipfs.cat(filePointer);
		return data;
	}
	
	public String addRecode(String recode) throws IOException {
		NamedStreamable.ByteArrayWrapper byteArray=new NamedStreamable.ByteArrayWrapper(recode.getBytes());
		MerkleNode addResult = ipfs.add(byteArray).get(0);
		System.out.println("MerkleNode:"+ JSONParser.toString(addResult));
		return addResult.hash.toString();
	}
	public String selectRecode(String hash) throws IOException {
		Multihash filePointer = Multihash.fromBase58(hash);
		byte[] data = ipfs.cat(filePointer);
		return new String(data);
	}

	/**
	 * 管理ipfs对象的固定
	 * @return
	 */
	public Map<String, PinKeys> getPins(){
		try {
			Map<String, PinKeys> pins = ipfs.pin();
			pins.forEach((k,v)->{
				System.out.println("pins v:"+ JSON.toJSONString(k)+",v:"+JSON.toJSONString(v));
			});
			return pins;
		}catch (Exception e){
			System.out.println("异常:"+e);
			return null;
		}
	}

	/**
	 * 判断操作系统是否是Windows
	 *
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	public static void createFile(File file) throws IOException {
		if (!file.exists()&&!file.getName().contains(".")) {
			// 能创建多级目录
			log.info(file.mkdirs()+file.getName());
		}
		//返回的是文件夹
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			// 能创建多级目录
			log.info(fileParent.mkdirs()+"创建文件夹"+fileParent.getName());
		}
		if (!file.exists()) {
			//有路径才能创建文件
			log.info(file.createNewFile()+"创建文件"+file.getName());
		}
	}
}
