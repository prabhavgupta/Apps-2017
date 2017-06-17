using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class AuthUserDTO
    {
        public string UserName { get; set; }
        public string Email { get; set; }

        public int? UserProfileId { get; set; }
        public UserProfile UserProfile { get; set; }
    }
}
