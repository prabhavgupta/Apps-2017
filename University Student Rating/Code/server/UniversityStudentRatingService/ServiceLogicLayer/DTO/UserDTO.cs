using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class UserDTO
    {
        public string UserName { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public GenderType GenderType { get; set; }
        public Country Country { get; set; }
        public City City { get; set; }
        public University University { get; set; }
        public Status Status { get; set; }
        public Faculty Faculty { get; set; }
        public Course Course { get; set; }
    }
}