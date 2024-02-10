package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation read(String id);
    Compensation create(Compensation comp);
    Compensation update(Compensation comp);
}
