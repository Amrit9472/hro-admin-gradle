package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.CandidatesDTO;

public interface CandidatesService {

	CandidatesDTO saveCandidates(CandidatesDTO candidatesDTO);

	List<CandidatesDTO> saveCandidatesMultiple(List<CandidatesDTO> candidatesDTO);

}