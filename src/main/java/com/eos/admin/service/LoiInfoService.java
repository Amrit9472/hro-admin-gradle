package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.LoiInfoDTO;
import com.eos.admin.dto.LoiInfoDropDownDTO;
import com.eos.admin.dto.LoiInformationDTO;
import com.eos.admin.dto.NameTypeDTO;

public interface LoiInfoService {

	LoiInfoDTO createLoiInfo(LoiInfoDTO loiInfoDTO);

	LoiInfoDTO getAllUniqueValueForDropDown();

	List<LoiInfoDropDownDTO> getAllGradValue(String process, String grad, String company);

	List<LoiInfoDropDownDTO> getAllUniqueDropDown();

	LoiInformationDTO getDetailsOfLoiInformation(String gridNo, String location);

	List<NameTypeDTO> getNamesByLocation(String location);

}
