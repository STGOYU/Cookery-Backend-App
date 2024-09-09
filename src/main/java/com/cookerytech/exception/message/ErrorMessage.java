package com.cookerytech.exception.message;

public class ErrorMessage {

    public final  static  String USER_NOT_FOUND_EXCEPTION="User with email: %s not found";

    public final  static  String ROLE_NOT_FOUND_EXCEPTION="Role: %s not found";
    public final static String JWTTOKEN_ERROR_MESSAGE = "JWT Token Validation Error: %s";

    public final static String EMAIL_ALREADY_EXIST_MESSAGE = "Email: %s already exist";

    public final static String PRINCIPAL_FOUND_MESSAGE = "User not found";
    public static final String RESOURCE_NOT_FOUND_EXCEPTION ="Resource with id %s not found" ;
    public static final String NOT_PERMITTED_METHOD_MESSAGE ="You don't have any permission to change this data";

    public final static String PASSWORD_NOT_MATCHED_MESSAGE = "Your password are not matched";
    public static final String NO_DATA_IN_DB_TABLE_MESSAGE ="Resource with id %s not found" ;

    public static final String TOKEN_NOT_MATCHED_MESSAGE = "Your token are not matched";

    public final  static  String MODEL_NOT_FOUND_EXCEPTION="Model: with id %s not found";
    public static final String NO_ACTIVE_BRANDS_MESSAGE = "There are no active brands";
    public static final String NO_ACTIVE_CATEGORY_MESSAGE = "There are no active category";

    public static final String CAN_NOT_UPDATE ="This data can not update";
    public static final String SKU_ALREADY_EXİST = "Sku already exist";
    public static final String BRAND_CAN_NOT_DELETED = "The Brand has related records in products table,";

    public final static String IMAGE_NOT_FOUND_MESSAGE = "ImageFile with id %s not found";

    public  final  static  String CAN_NOT_BE_DELETED_MESSAGE = "Cannot be deleted.Has related records in other table";



    public static final String CATEGORY_ALREADY_EXIST_MESSAGE = "Category already exist";

    public static final String CATEGORY_CANNOT_BE_DELETED_MESSAGE = "This category cannot be deleted.It has related records in other table";
    public static final String DELİVERY_DATE_INCORRECT_MESSAGE = "Delivery Date not correct";
    public static final String PRODUCT_PROPERTY_KEY_NAME_EXIST_MESSAGE = "Cannot be create this name";
    public static final String UNAUTH_TRANSACTİON = "unauthorized transaction";
}
