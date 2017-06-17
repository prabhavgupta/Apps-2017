using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ServiceLogicLayer.Services;
using ServiceLogicLayer.Interfaces;
using ServiceLogicLayer.DTO;
using DataAccessLayer.DbContext;
using USR_UnitTests.Helpers;

namespace USR_UnitTests
{
    [TestClass]
    public class AuthServiceTest
    {
        //IServiceCreator serviceCreator = new ServiceCreator();
        TestData TestData = new TestData();

        [TestMethod]
        public void Create()
        {
            //IAuthService authService = serviceCreator.CreateUserService();
            //UserDTO testingUserDTO = TestData.CreateUserDTO();

            //authService.CreateAsync(testingUserDTO, "123456");
        }
    }
}