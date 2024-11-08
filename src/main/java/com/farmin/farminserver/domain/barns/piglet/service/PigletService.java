package com.farmin.farminserver.domain.barns.piglet.service;

import com.farmin.farminserver.domain.barns.piglet.dto.PigletRequest;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletResponse;

public interface PigletService {
    PigletResponse createPiglet(PigletRequest dto);
    PigletResponse getPigletById(Integer id);
}
