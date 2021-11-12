package database;

import user.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseOperations {

    private static final String GET_USER_ID = "SELECT id FROM Donor WHERE email = ?";
    private static final String INSERT_DONOR_IN_DATABASE = "INSERT INTO Donor (firstName, lastName, phoneNumber, dateOfBirth, age, email, password, gender, idBloodType, idCounty, cnp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_COUNTY_ID = "SELECT id FROM County WHERE name = ?";
    private static final String GET_BLOOD_TYPE_ID = "SELECT id FROM BloodType WHERE type = ? and rh = ?";
    private static final String CHECK_IF_DONOR_IN_DATABASE_SIGN_UP = "SELECT * from Donor WHERE email = ? and password = ? or CNP = ?";
    private static final String CHECK_IF_DONOR_IN_DATABASE_SIGN_IN = "SELECT * from Donor WHERE email = ? and password = ?";
    private static final String CHECK_IF_EMAIL_IN_DATABASE = "SELECT * from Donor WHERE email = ?";
    private static final String GET_DONOR_DATA = "SELECT firstName, lastName, phoneNumber, dateOfBirth, age, email, password, gender, idBloodType, idCounty, cnp FROM Donor WHERE email = ?";
    private static final String GET_BLOOD_TYPE = "SELECT type, rh FROM BloodType WHERE id = ?";
    private static final String GET_COUNTY = "SELECT * FROM County WHERE id = ?";
    private static final String INSERT_COUNTY_IN_DATABASE = "INSERT INTO County VALUES(?, ?)";
    private static final String INSERT_APPOINTMENT_IN_DATABASE = "INSERT INTO Appointment VALUES(?, ?, ?, ?)";
    private static final String INSERT_CHECK_IN_FILE_IN_DATABASE = "INSERT INTO CheckInFile VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_USER_AAPOINTMENTS = "SELECT date, idBloodBank, idHospital FROM Appointment WHERE idDonor = ?";
    private static final String GET_BLOOD_BANKS = "SELECT name, idCounty, phoneNumber FROM BloodBank";
    private static final String GET_HOSPITALS = "SELECT name, idCounty, phoneNumber FROM Hospital";
    private static final String GET_OCCUPIED_ON_DATE = "SELECT date FROM Appointment WHERE (DATEPART(yy, date) = ? " +
            "and DATEPART(mm, date) = ? and DATEPART(dd, date) = ? and idBloodBank = ?)";
    private static final String UPDATE_USER_WEIGHT = "UPDATE Donor SET weight = ? WHERE email = ?";
    private static final String INIT_BLOOD_STOCK = "INSERT INTO BloodStock Values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String ADD_BLOODSTOCK = "UPDATE BloodStock SET ? = ? + 1 WHERE idBloodBank = ?";


    /**
     * Used only once, when populating the database with the counties information.
     */
    public void insertCountiesInDatabase() throws SQLException {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            Scraper scraper = new Scraper();
            ArrayList<County> counties = scraper.webScrapingCounties();
            for (County c : counties) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_COUNTY_IN_DATABASE)) {
                    preparedStatement.setString(1, c.getName());
                    preparedStatement.setInt(2, c.getNumberOfBloodBanks());
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    /**
     * Function to insert donor into Donor database, after successful sign up.
     *
     * @param donor - the donor that we want to insert into the database
     */
    public void insertDonorInDatabase(Donor donor) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement insertUser = conn.prepareStatement(INSERT_DONOR_IN_DATABASE)) {

            int idCounty = getDonorCountyId(donor);
            int idBloodType = getDonorBloodTypeId(donor);

            insertUser.setString(1, donor.getFirstName());
            insertUser.setString(2, donor.getLastName());
            insertUser.setString(3, donor.getPhoneNumber());
            insertUser.setDate(4, Date.valueOf(donor.getDateOfBirth()));
            insertUser.setInt(5, donor.getAge());
            insertUser.setString(6, donor.getEmail());
            insertUser.setString(7, donor.getPassword());
            insertUser.setString(8, donor.getGender());
            insertUser.setInt(9, idBloodType);
            insertUser.setInt(10, idCounty);
            insertUser.setString(11, donor.getCNP());

            insertUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the id of the donor's county
     */
    public int getDonorCountyId(Donor donor) throws SQLException {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_COUNTY_ID)) {
            preparedStatement.setString(1, donor.getCounty().getName());
            int idCounty = 0;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    idCounty = resultSet.getInt("id");
                }
            }
            return idCounty;
        }
    }

    /**
     * @return the id of the donor's blood type
     */
    public int getDonorBloodTypeId(Donor donor) throws SQLException {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_BLOOD_TYPE_ID)) {
            preparedStatement.setString(1, donor.getBloodType().getName());
            preparedStatement.setBoolean(2, donor.getBloodType().getRH());

            int idBloodType = 0;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    idBloodType = resultSet.getInt("id");
                }
            }
            return idBloodType;
        }
    }

    public BloodType getDonorBloodType(int idBloodType) throws SQLException {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_BLOOD_TYPE)) {
            preparedStatement.setInt(1, idBloodType);

            BloodType bloodType = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bloodType = new BloodType(resultSet.getString("type"), resultSet.getBoolean("rh"));
                }
            }
            return bloodType;
        }
    }

    public County getDonorCounty(int idCounty) throws SQLException {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_COUNTY)) {
            preparedStatement.setInt(1, idCounty);

            County county = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    county = new County(resultSet.getString("name"), resultSet.getInt("numberOfBloodBanks"));
                }
            }
            return county;
        }
    }

    /**
     * @return true if the donor is already found in the database. This is used only for sign up, by checking the combination email, password and
     * separately, the CNP.
     */
    public Boolean isDonorInDatabaseSignUp(Donor donor) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(CHECK_IF_DONOR_IN_DATABASE_SIGN_UP)) {
            preparedStatement.setString(1, donor.getEmail());
            preparedStatement.setString(2, donor.getPassword());
            preparedStatement.setString(3, donor.getCNP());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @return true if the donor is found in the database. This is used only for sign in, by checking the username and password
     * introduced by the donor.
     */
    public Boolean isDonorInDatabaseSignIn(Donor donor) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(CHECK_IF_DONOR_IN_DATABASE_SIGN_IN)) {
            preparedStatement.setString(1, donor.getEmail());
            preparedStatement.setString(2, donor.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isEmailInDatabase(Donor donor) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(CHECK_IF_EMAIL_IN_DATABASE)) {
            preparedStatement.setString(1, donor.getEmail());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get donor's date by email, after sign in. Then the static variable currentDonor is updated to this.
     */
    public Donor getDonorData(String email) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_DONOR_DATA)) {
            preparedStatement.setString(1, email);
            Donor donor = null;
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    donor = new Donor(r.getString("firstName"), r.getString("lastName"),
                            r.getString("phoneNumber"), r.getDate("dateOfBirth").toLocalDate(),
                            r.getString("email"), r.getString("password"), getDonorBloodType(r.getInt("idBloodType")), getDonorCounty(r.getInt("idCounty")),
                            r.getString("cnp"));
                }
            }
            return donor;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDonorsId(String email) {
        int id = 0;
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_USER_ID)) {
            preparedStatement.setString(1, email);
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    id = r.getInt("id");
                }
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<BloodBank> getBloodBanks(ArrayList<County> counties) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_BLOOD_BANKS)) {
            ArrayList<BloodBank> bloodBanks = new ArrayList<>();
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    BloodBank bldbnk = new BloodBank(r.getString("name"), r.getString("phoneNumber"), counties.get(r.getInt("idCounty") - 1));
                    bloodBanks.add(bldbnk);
                }
            }
            return bloodBanks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Hospital> getHospitals(ArrayList<County> counties) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_HOSPITALS)) {
            ArrayList<Hospital> hospital = new ArrayList<>();
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    Hospital newHospital = new Hospital(r.getString("name"), counties.get(r.getInt("idCounty") - 1), r.getString("phoneNumber"));
                    hospital.add(newHospital);
                }
            }
            return hospital;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Appointment> getUsersAppointments(Donor donor, ArrayList<BloodBank> bloodBanks, ArrayList<Hospital> hospitals) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_USER_AAPOINTMENTS)) {
            preparedStatement.setInt(1, getDonorsId(donor.getEmail()));
            ArrayList<Appointment> appointments = new ArrayList<>();
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    Appointment aux = new Appointment(r.getTimestamp("date"), donor, bloodBanks.get(r.getInt("idBloodBank") - 1), hospitals.get(r.getInt("idHospital") - 1));
                    appointments.add(aux);

                }
            }
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Timestamp> getOccupiedOnDate(String date, int idBloodBank) {
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(GET_OCCUPIED_ON_DATE)) {
            preparedStatement.setInt(1, Integer.parseInt(date.substring(0, 4)));
            preparedStatement.setInt(2, Integer.parseInt(date.substring(5, 7)));
            preparedStatement.setInt(3, Integer.parseInt(date.substring(8, 10)));
            preparedStatement.setInt(4, idBloodBank);
            ArrayList<Timestamp> dates = new ArrayList<>();
            try (ResultSet r = preparedStatement.executeQuery()) {
                while (r.next()) {
                    Timestamp timestamp = r.getTimestamp("date");
                    dates.add(timestamp);
                }
            }
            return dates;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertAppointment(Timestamp timestamp, int donorId, int bloodBankId, int idHospital) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_APPOINTMENT_IN_DATABASE)) {
                preparedStatement.setTimestamp(1, timestamp);
                preparedStatement.setInt(2, donorId);
                preparedStatement.setInt(3, bloodBankId);
                preparedStatement.setInt(4, idHospital);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void insertCheckInFile(int idDonor, boolean alcohol, boolean treatment, boolean tattoo, boolean rejected, boolean corona, boolean chronic, Timestamp timestamp) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CHECK_IN_FILE_IN_DATABASE)) {
                preparedStatement.setInt(1, idDonor);
                preparedStatement.setBoolean(2, alcohol);
                preparedStatement.setBoolean(3, treatment);
                preparedStatement.setBoolean(4, tattoo);
                preparedStatement.setBoolean(5, rejected);
                preparedStatement.setBoolean(6, corona);
                preparedStatement.setBoolean(7, chronic);
                preparedStatement.setTimestamp(8, timestamp);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void updateUserWeight(Donor donor, Integer weight) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_WEIGHT)) {
                preparedStatement.setInt(1, weight);
                preparedStatement.setString(2, donor.getEmail());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void initBloodBankQuantities(int bloodBankId) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(INIT_BLOOD_STOCK)) {
                preparedStatement.setInt(1, bloodBankId);
                preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);
                preparedStatement.setInt(5, 0);
                preparedStatement.setInt(6, 0);
                preparedStatement.setInt(7, 0);
                preparedStatement.setInt(8, 0);
                preparedStatement.setInt(9, 0);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public int getBloodStockCapacity(BloodType bloodType, int bloodBankId) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            String rh;
            if (bloodType.getRH()) rh = "Pos";
            else rh = "Neg";
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT quantity" + bloodType.getName() + rh + " FROM BloodStock WHERE idBloodBank = ?")) {
                preparedStatement.setInt(1, bloodBankId);
                try (ResultSet r = preparedStatement.executeQuery()) {
                    while (r.next()) {
                        if (bloodType.getRH())
                            return r.getInt("quantity" + bloodType.getName() + "Pos");
                        else r.getInt("quantity" + bloodType.getName() + "Neg");
                    }
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public void addBloodStock(int bloodBankId, BloodType bloodType) {
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            String statement = "";
            if (bloodType.getName().equals("A2")) {
                if (bloodType.getRH())
                    statement = "UPDATE BloodStock SET quantityA2Pos = quantityA2Pos + 1 WHERE idBloodBank = ?";
                else statement = "UPDATE BloodStock SET quantityA2Neg = quantityA2Neg + 1 WHERE idBloodBank = ?";
            } else if (bloodType.getName().equals("01")) {
                if (bloodType.getRH())
                    statement = "UPDATE BloodStock SET quantity01Pos = quantity01Pos + 1 WHERE idBloodBank = ?";
                else statement = "UPDATE BloodStock SET quantity01Neg = quantity01Neg + 1 WHERE idBloodBank = ?";
            } else if (bloodType.getName().equals("B3")) {
                if (bloodType.getRH())
                    statement = "UPDATE BloodStock SET quantityB3Pos = quantityB3Pos + 1 WHERE idBloodBank = ?";
                else statement = "UPDATE BloodStock SET quantityB3Neg = quantityB3Neg + 1 WHERE idBloodBank = ?";
            }
            if (bloodType.getName().equals("AB4")) {
                if (bloodType.getRH())
                    statement = "UPDATE BloodStock SET quantityAB4Pos = quantityAB4Pos + 1 WHERE idBloodBank = ?";
                else statement = "UPDATE BloodStock SET quantityAB4Neg = quantityAB4Neg + 1 WHERE idBloodBank = ?";
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                //preparedStatement.setInt(1, getBloodStockCapacity(bloodType, bloodBankId) + 1);
                preparedStatement.setInt(1, bloodBankId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
