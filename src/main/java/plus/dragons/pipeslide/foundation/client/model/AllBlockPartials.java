package plus.dragons.pipeslide.foundation.client.model;

import plus.dragons.pipeslide.PipeSlide;

public class AllBlockPartials {

    private static PartialModel block(String path) {
        return new PartialModel(PipeSlide.genRL("block/" + path));
    }

    public static void init() {}

}
