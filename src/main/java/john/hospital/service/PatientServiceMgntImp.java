package john.hospital.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import john.hospital.converter.ConverterUtil;
import john.hospital.dbObjects.FileInfo;
import john.hospital.dbObjects.Patient;
import john.hospital.dto.FileInfoDTO;
import john.hospital.dto.PatientDTO;
import john.hospital.dto.ResponseDTO;
import john.hospital.repository.IFileInfoRepository;
import john.hospital.repository.IPatientRepository;

@Service
public class PatientServiceMgntImp implements IPatientServiceMgnt {

	
	
	@Autowired
	private IPatientRepository patientRepo;
	@Autowired
	private IFileInfoRepository fileRepo;
	
	@Override
	public String register(PatientDTO dto) throws IllegalStateException, IOException {
		 
		String msg = null;
		if(dto.getFile().getOriginalFilename().contains(".pdf"))
		{
			Patient patient = ConverterUtil.convertDTOToDBO(dto);
			
			dto.getFile().transferTo(new File(patient.getFile().getFilePath()));
			Integer id = patientRepo.save(patient).getPid();
			msg = "Patient and its associated file information uploaded successfully-"+id;
		}
		else
			msg = "Attemt wrong file uploading! You are allowed to upload pdf file only";
		
		return msg;
		 
	}

	@Override
	public List<ResponseDTO> getAllPatients() {
		  
		     List<Patient> pList = patientRepo.getAllPatients();
		     List<ResponseDTO> resList = ConverterUtil.convertDBOListToDTOList(pList);
		    
		return resList;
	}

	@Override
	public ResponseDTO getSinglePatient(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FileInfoDTO getFileByUniqueFileName(String uniqueFileName)
	{
		   Optional<FileInfo> opt = fileRepo.findByUniqueFileName(uniqueFileName);
		   FileInfoDTO fDTO = null;
		   if(opt.isPresent())
		   {
			   fDTO = ConverterUtil.convertFileInfoDBOToFileInfoDTO(opt.get());
		   }
		   
		   return fDTO;
	}

	@Override
	public String delete(Integer id) {
		 
		String status = null;
		Optional<Patient> opt = patientRepo.findById(id);
		if(opt.isPresent())
		{
			Patient dbo = opt.get();
		     dbo.setRecordStatus("D");
		     dbo.getFile().setRecordStatus("D");
		     patientRepo.save(dbo);
			status = "success";
			
		}
		else
		{
			status = "failed";
		}
		return status;
	}

}
