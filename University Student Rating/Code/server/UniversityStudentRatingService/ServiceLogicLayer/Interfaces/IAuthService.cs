using DataAccessLayer.Entities;
using Microsoft.AspNet.Identity;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models.AccountModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface IAuthService : IDisposable
    {
        Task<IdentityResult> CreateAsync(RegisterModel model);
        Task<ApplicationUser> FindAsync(string userName, string password);
        Task<ClaimsIdentity> Authenticate(LoginModel model);
    }
}
