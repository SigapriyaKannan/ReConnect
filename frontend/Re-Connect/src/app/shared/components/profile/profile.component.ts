import {City, Company, Country, Skill, UserDetails} from "./user-details.model";
import {ActivatedRoute} from "@angular/router";
import {ProfileService} from "./profile.service";
import {MessageService} from "primeng/api";
import {CompanyService} from "../../services/company.service";
import {SkillsService} from "../../services/skills.service";
import {CountryService} from "../../services/country.service";
import {CityService} from "../../services/city.service";
import {Experience} from "../../services/experience.service";
import {Button} from "primeng/button";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {CardModule} from "primeng/card";
import {FileUploadModule} from "primeng/fileupload";
import {DropdownModule} from "primeng/dropdown";
import {MultiSelectModule} from "primeng/multiselect";
import {Component, OnInit} from "@angular/core";
import {forkJoin, Observable} from "rxjs";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss'],
    standalone: true,
    imports: [
        Button,
        FormsModule,
        NgIf,
        CardModule,
        FileUploadModule,
        DropdownModule,
        ReactiveFormsModule,
        NgForOf,
        MultiSelectModule
    ],
    providers: [MessageService]
})
export class ProfileComponent implements OnInit {
    profileForm!: FormGroup;
    userDetails: UserDetails = {
        detailId: 0,
        userName: '',
        experience: 0,
        resume: '',
        profilePicture: '',
        city: 0,
        country: 0,
        company: 0,
        cityName: '',
        countryName: '',
        companyName: '',
        skills: []
    };

    profilePictureUrl: any;
    resumeUrl: any;
    editMode: boolean = false;
    showUser: boolean = false;

    listOfExperiences: Experience[] = [
        {"experienceId": 1, "experienceName": "Entry Level"},
        {"experienceId": 2, "experienceName": "Mid Level"},
        {"experienceId": 3, "experienceName": "Senior Level"},
        {"experienceId": 4, "experienceName": "Executive Level"}
    ];
    listOfCompanies: Company[] = [];
    listOfCountries: Country[] = [];
    listOfCities: City[] = [];
    listOfSkills: Skill[] = [];

    constructor(
        private activatedRoute: ActivatedRoute,
        private profileService: ProfileService,
        private messageService: MessageService,
        private companyService: CompanyService,
        private skillsService: SkillsService,
        private countryService: CountryService,
        private cityService: CityService
    ) {
    }

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(({ showUser }) => {
            this.showUser = showUser;
        });

        this.profileForm = new FormGroup({
            userName: new FormControl(this.userDetails.userName, [Validators.required]),
            company: new FormControl(null, [Validators.required]),
            experience: new FormControl(null, [Validators.required]),
            skills: new FormControl([], [Validators.required]),
            country: new FormControl(null, [Validators.required]),
            city: new FormControl(null, [Validators.required]),
            profilePicture: new FormControl(null),
            resume: new FormControl(null)
        });

        this.fetchDropdownOptionsAndUserDetails();
    }


    fetchDropdownOptionsAndUserDetails() {
        forkJoin({
            companies: this.companyService.getAllCompanies(),
            countries: this.countryService.getAllCountries(),
            skills: this.skillsService.getAllSkills(),
            cities: this.cityService.getAllCities(),
            userDetails: this.profileService.getUserDetails()
        }).subscribe(({companies, countries, skills, cities, userDetails}) => {
            this.listOfCompanies = companies['body'];
            this.listOfCountries = countries['body'];
            this.listOfSkills = skills['body'];
            this.listOfCities = cities['body'];
            this.userDetails = userDetails;

            const company = this.listOfCompanies.find(c => c.companyId === userDetails.company);
            const country = this.listOfCountries.find(c => c.countryId === userDetails.country);
            const city = this.listOfCities.find(c => c.cityId === userDetails.city);

            this.userDetails.companyName = company ? company.companyName : "";
            this.userDetails.countryName = country ? country.countryName : "";
            this.userDetails.cityName = city ? city.cityName : "";

            this.profileForm.patchValue({
                userName: userDetails.userName,
                company: company ? company : null,
                experience: userDetails.experience,
                country: country ? country: null,
                city: city ? city : null,
                skills: userDetails.skills?.map(skill => skill.skillId) || []
            });

            this.getProfilePicture();

            // Load cities based on the initial country value
            if (userDetails.country) {
                this.loadCities(userDetails.country);
            }

            // Listen for country changes to load cities dynamically
            this.profileForm.get('country')?.valueChanges.subscribe(({countryId}) => {
                this.loadCities(countryId);
            });
        });
    }

    loadCities(countryId: number) {
        this.cityService.getAllCities(countryId).subscribe((response: any[]) => {
            this.listOfCities = response['body'];
        });


    }

    updateUserDetails() {
        const formValues = this.profileForm.value;

        // Construct the object according to UserDetailsRequest
        const updatedUserDetailsRequest = {
            userName: formValues.userName,
            experience: formValues.experience?.experienceId || formValues.experience, // Assuming experience is an integer
            company: formValues.company?.companyId || formValues.company, // Extract ID or use directly if it's already an ID
            city: formValues.city?.cityId || formValues.city, // Extract ID or use directly if it's already an ID
            country: formValues.country?.countryId || formValues.country, // Extract ID or use directly if it's already an ID
            skillIds: formValues.skills || [] // Directly use skill IDs array
        };

        this.profileService.updateUserDetails(updatedUserDetailsRequest).subscribe((data: UserDetails) => {
            // Fetch the updated user details after the update
            //this.fetchUserDetails(); // Method to refresh user details

            this.userDetails = data;

            // Update dropdowns and form values
            const company = this.listOfCompanies.find(c => c.companyId === data.company);
            const country = this.listOfCountries.find(c => c.countryId === data.country);
            const city = this.listOfCities.find(c => c.cityId === data.city);

            this.userDetails.companyName = company ? company.companyName : "";
            this.userDetails.countryName = country ? country.countryName : "";
            this.userDetails.cityName = city ? city.cityName : "";
            this.editMode = false;
            this.messageService.add({
                severity: 'success',
                summary: 'Success',
                detail: 'User details updated successfully'
            });
        });
    }

    fetchUserDetails() {
        this.profileService.getUserDetails().subscribe((data: UserDetails) => {
            this.userDetails = data;

            // Update dropdowns and form values
            const company = this.listOfCompanies.find(c => c.companyId === data.company);
            const country = this.listOfCountries.find(c => c.countryId === data.country);
            const city = this.listOfCities.find(c => c.cityId === data.city);

            this.userDetails.companyName = company ? company.companyName : "";
            this.userDetails.countryName = country ? country.countryName : "";
            this.userDetails.cityName = city ? city.cityName : "";

            this.profileForm.patchValue({
                userName: data.userName,
                company: company ? company : null,
                experience: data.experience,
                country: country ? country : null,
                city: city ? city : null,
                skills: data.skills?.map(skill => skill.skillId) || []
            });

            this.getProfilePicture();

            // Load cities based on the updated country value
            if (data.country) {
                this.loadCities(data.country);
            }
        });
    }




    uploadResume(event: any) {
        const file: File = event.files[0];
        this.profileService.uploadResume(file).subscribe(response => {
            const message = 'Resume uploaded successfully';
            this.messageService.add({severity: 'success', summary: 'Success', detail: message});
        });
    }

    uploadProfilePicture(event: any) {
        const file: File = event.files[0];
        this.profileService.uploadProfilePicture(file).subscribe(response => {
            const message = 'Profile picture uploaded successfully';
            this.messageService.add({severity: 'success', summary: 'Success', detail: message});
            this.getProfilePicture();
        });
    }

    getResume() {
        this.profileService.getResume().subscribe((blob: Blob) => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'resume.pdf';
            a.click();
            window.URL.revokeObjectURL(url);
        });
    }

    getProfilePicture() {
        this.profileService.getProfilePicture().subscribe((blob: Blob) => {
            this.profilePictureUrl = URL.createObjectURL(blob);
        });
    }
}
