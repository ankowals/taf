package data.builders;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.Category;

import static io.qala.datagen.RandomValue.between;
import static io.qala.datagen.RandomValue.length;

@NoArgsConstructor
@Getter
@Builder
public class CustomCategory {

    Long id;
    String name;

    //we have to overwrite build() method generated by lombok because we want to return a different type
    public static class CustomCategoryBuilder {
        Long id = between(0, 100000000).Long();     // let us set default random value
        String name = length(5).english();          // let us set default random value
        public Category build() { return new Category().name(name).id(id); }
    }

}
