package com.skku.fixskkufront;

public class ChatbotResponse {
    private int code;
    private String message;
    private Data data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        private String response;
        private String uri;
        private String campus;
        private String building;
        private String classroom;

        public String getResponse() {
            return response;
        }

        public String getUri() {
            return uri;
        }

        public String getCampus() {
            return campus;
        }

        public String getBuilding() {
            return building;
        }

        public String getClassroom() {
            return classroom;
        }
    }
}