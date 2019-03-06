package ittalents.finalproject.controller;

public class FilterPetsController extends BaseController {
    //    @GetMapping(value = "/{breed}")
//    public List<Pet> getByBreed(@RequestParam String breed){
//        return dao.getFiltred(breed, null, -1, -1, null, -1,
//                -1, null, null);
//    }

//    @GetMapping(value = "/{breed}/{subBreed}")
//    public List<Pet> getByBreedSubBreed(@PathVariable String breed, @PathVariable String subBreed){
//        return dao.getFiltred(breed, subBreed);
//    }


//    public List<Pet> getFiltred(String breed, String subBreed, int fromAge, int toAge, String gender,
//                                double fromPrice, double toPrice, String sortBy, String ascDesc) {
//
//        String selectClause = "SELECT * FROM EMPLOYEES WHERE ";
//        if(StringUtils.isNotBlank(empName)){
//            selectQuery += "AND EMP_NAME = " + empName;
//        }
//        if(StringUtils.isNotBlank(empID)){
//            selectQuery += "AND EMP_ID = " + empID;
//
//        String filter;
//        List<Pet> pets = null;
//        if(subBreed != null && fromAge > -1 && toAge > -1 && gender != null && fromPrice > 0 && toPrice > 0 &&
//            sortBy != null && ascDesc != null) {
//            if (fromAge > toAge) {
//                int temp = fromAge;
//                fromAge = toAge;
//                toAge = temp;
//            } else if (fromPrice > toPrice) {
//                double temp = fromPrice;
//                fromPrice = toPrice;
//                toPrice = temp;
//            }
//            filter = "SELECT id, gender, breed, sub_breed, age, posted, pet_desc, in_sale, price, quantity FROM pets WHERE (age >= ? AND age <= ?)" +
//                    " AND breed LIKE ? AND sub_breed LIKE ? AND gender LIKE ? && (price >= ? AND price <= ?) ORDER BY ? ?" +
//                    " ORDER BY ? ?";
//            pets = db.query(filter, new Object[]{fromAge, toAge, breed, subBreed, gender, fromPrice, toPrice}, (resultSet, i) -> toPet(resultSet));
//        }
////       else if(){
////
//       }
//
//
//
//       return pets;
//    }
}
