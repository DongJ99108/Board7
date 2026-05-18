package com.green.pds.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.green.pds.dto.FilesDto;
import com.green.pds.dto.PdsDto;
import com.green.pds.mapper.PdsMapper;
import com.green.pds.service.PdsService;

@Service
public class PdsServiceImpl implements PdsService {
	
	// @Value가 application.properties 에 있는
	// part1.upload-path=D:/2026/dev/springboot/data/
	// import org.springframework.beans.factory.annotation.Value;
	@Value("${part1.upload-path}")
	private String uploadPath;
	
	@Autowired
	private PdsMapper pdsMapper;

	@Override
	public List<PdsDto> getPdsList(HashMap<String, Object> map) {
		List<PdsDto> pdsList = pdsMapper.getPdsList(map);
		return       pdsList;
	}

	@Override
	public void setWrite(HashMap<String, Object> map, MultipartFile[] uploadfiles) {
		// 파일저장 + db 저장
		// 1. 파일저장 : uploadfiles [] -> uploadPath = d:/2026/dev/springboot/data/
		
		// String uploadPath = "d:/2026/dev/springboot/data/";
		map.put("uploadPath", uploadPath);
		
		System.out.println("PdsFile 이전 map:" + map);
		
		// 별도 클래스 생성해서 처리 : PdsFile
		// 넘어온 정보로 파일을 저장
		PdsFile.save( map, uploadfiles );
		
		System.out.println("PdsFile 이후 map:" + map);
		// {
		// menu_id=MENU01, nowpage=1, title=abcd, writer=abcdd, content=grbsdav brwabfds abgrwanmytyr,
		// uploadPath=D:/2026/dev/springboot/data, 
		// fileList=[
		//   FilesDto(file_num=0, idx=0, filename=axios설명.txt, 
		//     fileext=.txt, sfilename=2026\05\18\04ecc723-5fe4-4421-8995-7ccc33060a86.axios설명.txt), 
		//   FilesDto(file_num=0, idx=0, filename=data.csv, 
		//     fileext=.csv, sfilename=2026\05\18\41bef69a-e136-41bc-83c1-8917b38b2540.data.csv)
		//   ]
		// }
		
		// 2. db 저장 : 자료실 글 쓰기 <- map
		//     Board 테이블에 저장
		pdsMapper.setWrite( map );    // insertBoard
		
		// 3. Files 에 저장
	    List<FilesDto> fileList = (List<FilesDto>) map.get("fileList");
	    if( fileList.size() > 0 )
	    	pdsMapper.setFileWriter( map );
		
	}
	
}



















