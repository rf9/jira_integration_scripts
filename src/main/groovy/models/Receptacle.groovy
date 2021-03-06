/**
 * See README.md for copyright details
 */
package models;

import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

/**
 * Model for the {@code Receptacle} entity used by the JSON API Converter.
 * 
 * @author ke4
 *
 */
@Type("receptacles")
class Receptacle extends BaseModel {

    @Id
    String id;
    String material_uuid;

    @Relationship("labware")
    Labware labware;

    @Relationship("location")
    Location location;
}
