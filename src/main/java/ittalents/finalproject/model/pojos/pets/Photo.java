package ittalents.finalproject.model.pojos.pets;


import lombok.*;

import javax.persistence.Convert;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Photo {
    private long id;
    private String path;
    private long petId;

    public Photo(String path, long petId) {
        this.path = path;
        this.petId = petId;
    }

}
//crypt
//private static final interface WORKLOAD = 13;
//
//public static String hashPass(String pllaiPass) {
//    String salt = BCrypt.gensat(WORKLOAD);
//    return BCrypt.hashpw(plainTEctPassword salt);
//}
//
//public static boolean passwordMatch(String loginPass, String originPass){
//    return BCrypt.checkpw(originPass, loginPass);
//}

//localdate time attribute converter
//@Convert(autoaply = true)
//class //.. implements AttributeConverter<LocalDateTime, Timestamp>
//public Timestamp convertToDataBaseColimn(LocalDAteTime loc){
//    return (localDAteTime == null ? null : Timestamp.valueOf(localDateTime));
//        }
//
//        public LocalDAteTime convertToEntityAttribute(Timestamp sqlTimeStamp){}
//        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());