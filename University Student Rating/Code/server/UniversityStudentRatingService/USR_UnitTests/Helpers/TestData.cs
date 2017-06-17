using DataAccessLayer.DbContext;
using ServiceLogicLayer.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace USR_UnitTests.Helpers
{
    class TestData
    {
        public UserDTO CreateUserDTO()
        {
            UserDTO testingUser = new UserDTO
            {
                UserName = "qwerty",
                City = new City
                {
                    Name = "city"
                },
                Country = new Country
                {
                    Name = "country"
                },
                Email = "email@mail.ru",
                Course = new Course
                {
                    Number = 1
                },
                FirstName = "fName",
                LastName = "lName",
                Faculty = new Faculty
                {
                    Name = "faculty"
                },
                GenderType = new GenderType
                {
                    Name = "male"
                },
                Status = new Status
                {
                    Name = "professor"
                },
                University = new University
                {
                    Name = "university"
                }
            };
            return testingUser;
        }
    }
}
