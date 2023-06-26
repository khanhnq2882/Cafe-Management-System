export class GlobalConstants{

    //Message
    public static genericError: string = "Something went wrong. Please try again!";

    //Regex
    public static nameRegex: string = "^[a-zA-Z0-9 ]+$";
    
    public static emailRegex: string = "^[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}$";

    public static contactNumberRegex: string = "^(0[3|5|7|8|9])+([0-9]{8})$";

    public static passwordRegex: string = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]).{8,25}$";

    //Variable
    public static error: string = 'error';
}