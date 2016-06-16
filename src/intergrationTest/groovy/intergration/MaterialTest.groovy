package intergration

import actions.LabwareActions
import actions.MaterialActions
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.jasminb.jsonapi.ResourceConverter
import models.Material
import models.MaterialBatch
import models.MaterialType
import models.Metadatum
import spock.lang.Specification
import utils.RestService
import utils.RestServiceConfig

/**
 * Created by rf9 on 16/06/2016.
 */
class MaterialTest extends Specification {
    def "should be able to create a material"() {
        given:
        String materialName = "test_name_${(int) (Math.random() * 1000000000)}"

        def material = new Material(
            name: materialName,
            materialType: new MaterialType(name: 'sample'),
            metadata: [new Metadatum(key: 'test_key_1', value: 'test_value_1'), new Metadatum(key: 'test_key_2', value: 'test_value_2')]
        )

        when:
        material = MaterialActions.postMaterials([material])[0]

        then:
        material.id != null
        material.name == materialName
        material.materialType.name == 'sample'
        material.metadata.size() == 2
        material.metadata[0].key == 'test_key_1'
        material.metadata[0].value == 'test_value_1'
        material.metadata[1].key == 'test_key_2'
        material.metadata[1].value == 'test_value_2'
    }

    def "should be able to make a material and then add a metadata"() {
        given:
        String materialName = "test_name_${(int) (Math.random() * 1000000000)}"

        def material = new Material(
            name: materialName,
            materialType: new MaterialType(name: 'sample')
        )
        material = MaterialActions.postMaterials([material])[0]

        def newMaterial = new Material(id: material.id, metadata: [new Metadatum(key: 'test_key_1', value: 'test_value_1'), new Metadatum(key: 'test_key_2', value: 'test_value_2')])

        when:
        newMaterial =  MaterialActions.postMaterials([newMaterial])[0]

        then:
        newMaterial.id == material.id
        material.name == newMaterial.name
        material.metadata.size() == 0
        newMaterial.metadata.size() == 2
        newMaterial.metadata[0].key == 'test_key_1'
        newMaterial.metadata[0].value == 'test_value_1'
        newMaterial.metadata[1].key == 'test_key_2'
        newMaterial.metadata[1].value == 'test_value_2'
    }

    def "should be able to parent a material"() {
        given:
        String materialName = "test_name_${(int) (Math.random() * 1000000000)}"

        def parentMaterial = new Material(
            name: materialName,
            materialType: new MaterialType(name: 'sample')
        )
        parentMaterial = MaterialActions.postMaterials([parentMaterial])[0]

        def childMaterial = new Material(
            name: (String)"${materialName}_child",
            materialType: new MaterialType(name: 'sample'),
            parents: [new Material(id: parentMaterial.id)]
        )

        when:
        childMaterial = MaterialActions.postMaterials([childMaterial])[0]

        then:
        childMaterial.parents[0].id == parentMaterial.id
        MaterialActions.getMaterials([parentMaterial.id])[0].children[0].id == childMaterial.id
    }
}
