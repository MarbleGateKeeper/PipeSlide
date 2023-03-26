/*
Copyright (c) 2021 Jozufozu

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package plus.dragons.pipeslide.foundation.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reference:
 * Version: com.jozufozu.flywheel:flywheel-forge-1.19.2:0.6.8.a-14
 * Class: com.jozufozu.flywheel.core.PartialModel
 * <p>
 * A helper class for loading and accessing json models.
 * <br>
 * Creating a PartialModel will make the associated modelLocation automatically load.
 * PartialModels must be initialized the mod class constructor.
 * <br>
 * Once {@link ModelEvent.BakingCompleted} finishes, all PartialModels (with valid modelLocations)
 * will have their bakedModel fields populated.
 * <br>
 * Attempting to create a PartialModel after {@link ModelEvent.RegisterAdditional} will cause an error.
 */
public class PartialModel {

    private static final List<PartialModel> ALL = new ArrayList<>();
    private static boolean tooLate = false;

    protected final ResourceLocation modelLocation;
    protected BakedModel bakedModel;

    public PartialModel(ResourceLocation modelLocation) {
        if (tooLate) throw new RuntimeException("PartialModel '" + modelLocation + "' loaded after ModelEvent.RegisterAdditional");

        this.modelLocation = modelLocation;
        ALL.add(this);
    }

    public static void onModelRegistry(ModelEvent.RegisterAdditional event) {
        for (PartialModel partial : ALL)
            event.register(partial.getLocation());

        tooLate = true;
    }

    public static void onModelBake(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> models = event.getModels();
        for (PartialModel partial : ALL)
            partial.set(models.get(partial.getLocation()));
    }

    protected void set(BakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    public ResourceLocation getLocation() {
        return modelLocation;
    }

    public BakedModel get() {
        return bakedModel;
    }

}
