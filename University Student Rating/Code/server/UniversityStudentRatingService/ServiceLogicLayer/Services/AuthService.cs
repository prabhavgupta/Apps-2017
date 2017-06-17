using AutoMapper;
using DataAccessLayer.DbContext;
using DataAccessLayer.Entities;
using DataAccessLayer.Interfaces;
using Microsoft.AspNet.Identity;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Interfaces;
using ServiceLogicLayer.Models.AccountModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Services
{
    public class AuthService : IAuthService
    {
        IUnitOfWork Database { get; set; }

        public AuthService(IUnitOfWork uow)
        {
            Database = uow;
        }

        public async Task<IdentityResult> CreateAsync(RegisterModel model)
        {
            try
            {
                if (model.StatusId == 2 && model.SpecialKEY != "TSI007")
                {
                    return new IdentityResult(new[] { "SpecialKEY is incorrect" });
                }
                ApplicationUser user = Mapper.Map<ApplicationUser>(model);
                var result = await Database.ApplicationUserManager.CreateAsync(user, model.Password);

                if (result.Succeeded)
                {
                    UserProfile profile = Mapper.Map<UserProfile>(model);
                    profile.PhotoPath = "UserPhotos/default_user_photo.png";
                    if (model.StatusId == 2)
                    {
                        profile.FacultyId = null;
                        profile.CourseId = null;
                    }
                    Database.UserRepository.Create(profile);
                    user.UserProfile = profile;
                    await Database.SaveAsync();
                }

                return result;
            }
            catch (Exception e)
            {
                return new IdentityResult(new [] {"Database error"});
            }
        }

        public async Task<ApplicationUser> FindAsync(string userName, string password)
        {
            return await Database.ApplicationUserManager.FindAsync(userName, password);
        }

        public async Task<ClaimsIdentity> Authenticate(LoginModel model)
        {
            ClaimsIdentity claim = null;
            ApplicationUser user = await Database.ApplicationUserManager.FindAsync(model.UserName, model.Password);
            if (user != null)
            {
                claim = await Database.ApplicationUserManager.CreateIdentityAsync(user, DefaultAuthenticationTypes.ApplicationCookie);
            }
            return claim;
        }

        public void Dispose()
        {
            Database.Dispose();
        }
    }
}