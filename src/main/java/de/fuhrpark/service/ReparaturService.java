package de.fuhrpark.service;

import de.fuhrpark.model.ReparaturBuchEintrag;
import java.util.List;

public interface ReparaturService {
    void addReparatur(ReparaturBuchEintrag eintrag);
    List<ReparaturBuchEintrag> getReparaturenForFahrzeug(String kennzeichen);
}