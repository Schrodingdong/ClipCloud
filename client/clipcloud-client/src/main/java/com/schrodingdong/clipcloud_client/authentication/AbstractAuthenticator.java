package com.schrodingdong.clipcloud_client.authentication;

public abstract class AbstractAuthenticator {
    private static String accessToken = "eyJraWQiOiJGaGJCaVBaRlhyZjVwc1RheWZjTmNyYUpDZTdMdnpkY2tDNEVFQ1RxeHY4PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJjODZkZGY4Yy1hYWExLTQ5ZjktOGYyOC0yMGFkZjkyOGE0NWIiLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtd2VzdC0yLmFtYXpvbmF3cy5jb21cL2V1LXdlc3QtMl9GZHlITmlVWDAiLCJjbGllbnRfaWQiOiI2MmlxaGw2MmN2YmE4azM3aWZvN2xrZjVqcSIsIm9yaWdpbl9qdGkiOiJlZDY2MjZjMC03MzNhLTRiODItODQ5Zi1hNDFkOTViOTA2YmUiLCJldmVudF9pZCI6IjZhM2MyMjY2LTJmOGMtNGIxYS05NmEwLTVjY2ZiY2UwNWQ2ZSIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2OTk3MzM0ODIsImV4cCI6MTY5OTczNzA4MSwiaWF0IjoxNjk5NzMzNDgyLCJqdGkiOiI4NWE5NmVmOC0zYTJhLTRkNjktYjJkMy0wYmM0NTg2OWRhM2MiLCJ1c2VybmFtZSI6ImM4NmRkZjhjLWFhYTEtNDlmOS04ZjI4LTIwYWRmOTI4YTQ1YiJ9.UMQzaCSol0_TozZMpuf3cZxpzakrVUUEpEb2cXJ_YrT8e2IiSrW_cCtzzdpYQJ1icZWWhYgF4xru0kF7Nu_iyJkyNIrJ2ZJVJweDOyWTreoHUn08SQ8AK5NWleQnoo9D-EeL0rMcooK7ez9RjpSNiLABCJ96dKRrkfWRNYkyNOP4aGbU5hAuPxGapOMqgq_9lxKtQPkAFAU1NUNH1ZvkSWJKyI9Q0GJCFBjLAuGZeh2MnXwZa0p5GO6J5-OXfLpGtIySThYdegmA6dU39jSMxwjVtUYlpMEfjLNZqS7vFvwQ8HtoEA7sCGUVJCYid99iK3tdLTRq1dlp8icDdzM6LQ";
    private static String refreshToken;
    private static String idToken;
    public static void setAccessToken(String token){
        accessToken = token;
    }
    public static String getAccessToken(){
        return accessToken;
    }
    public static void setIdToken(String token){
        idToken = token;
    }
    public static String getIdToken(){
        return idToken;
    }
    public static void setRefreshToken(String token){
        refreshToken = token;
    }
    public static String getRefreshToken(){
        return refreshToken;
    }

    public abstract String logIn(AuthModel authParam);
    public abstract boolean signUp(AuthModel authParam);
}
