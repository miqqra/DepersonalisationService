package ru.nsu.backend.converter;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

class File2TableConverterTest {

    @Test
    void testCsv() {
        String people = """
                "ID","Surname","Name","Patronymic","Age","Sex","Birthday","Passport series","Passport number","Where issued","When issued","Registration","Work","Taxpayer Identification Number","SNILS"
                "1","1","1","1","1","M","0001-12-30","1","1","Kutateladze 1","0001-12-30","Kutateladze 1","1","1","1"
                "2","2","2","2","2","M","0002-12-30","2","2","Kutateladze 2","0002-12-30","Kutateladze 2","2","2","2"
                "3","3","3","3","3","M","0003-12-30","3","3","Kutateladze 3","0003-12-30","Kutateladze 3","3","3","3"
                "4","4","4","4","4","M","0004-12-30","4","4","Kutateladze 4","0004-12-30","Kutateladze 4","4","4","4"
                "5","5","5","5","5","M","0005-12-30","5","5","Kutateladze 5","0005-12-30","Kutateladze 5","5","5","5"
                "6","6","6","6","6","M","0006-12-30","6","6","Kutateladze 6","0006-12-30","Kutateladze 6","6","6","6"
                "7","7","7","7","7","M","0007-12-30","7","7","Kutateladze 7","0007-12-30","Kutateladze 7","7","7","7"
                "8","8","8","8","8","M","0008-12-30","8","8","Kutateladze 8","0008-12-30","Kutateladze 8","8","8","8"
                "9","9","9","9","9","M","0009-12-30","9","9","Kutateladze 9","0009-12-30","Kutateladze 9","9","9","9"
                "10","10","10","10","10","M","0010-12-30","10","10","Kutateladze 10","0010-12-30","Kutateladze 10","10","10","10"
                "11","11","11","11","11","M","0011-12-30","11","11","Kutateladze 11","0011-12-30","Kutateladze 11","11","11","11"
                "12","12","12","12","12","M","0012-12-30","12","12","Kutateladze 12","0012-12-30","Kutateladze 12","12","12","12"
                "13","13","13","13","13","M","0013-12-30","13","13","Kutateladze 13","0013-12-30","Kutateladze 13","13","13","13"
                "14","14","14","14","14","M","0014-12-30","14","14","Kutateladze 14","0014-12-30","Kutateladze 14","14","14","14"
                "15","15","15","15","15","M","0015-12-30","15","15","Kutateladze 15","0015-12-30","Kutateladze 15","15","15","15"
                "16","16","16","16","16","M","0016-12-30","16","16","Kutateladze 16","0016-12-30","Kutateladze 16","16","16","16"
                "17","17","17","17","17","M","0017-12-30","17","17","Kutateladze 17","0017-12-30","Kutateladze 17","17","17","17"
                "18","18","18","18","18","M","0018-12-30","18","18","Kutateladze 18","0018-12-30","Kutateladze 18","18","18","18"
                "19","19","19","19","19","M","0019-12-30","19","19","Kutateladze 19","0019-12-30","Kutateladze 19","19","19","19\"""";
        System.out.println(File2TableConverter.convert(new ByteArrayInputStream(people.getBytes()), "csv"));
    }

}